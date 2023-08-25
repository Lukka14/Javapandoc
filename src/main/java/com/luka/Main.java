package com.luka;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static final String CMD_PREFIX = "cmd /c ";
    public static final boolean commandDebug = false;

    public static void main(String[] args) throws IOException, InterruptedException {
        String projectName = "Javapandoc";
        String pandocFilePath = "C:\\Users\\USER.LUKA\\AppData\\Local\\Pandoc\\";
        String outputpath = pandocFilePath+projectName;

        String sourcepath  = "C:\\Users\\USER.LUKA\\Desktop\\javadoc";

        List<String> allPackages = getSubDirectories(sourcepath);
        String allclasses = "allclasses";

        for(String packageName : allPackages) {
            String outPutFilePath = outputpath + "\\" + packageName; // C:\Users\USER.LUKA\Desktop\javadoc\driver-facebook
            String allClassesFilePath = sourcepath + "\\" + packageName + "\\" + allclasses + ".html";
//            String mediaWikiDirectoryName = "mediawiki";
            run("cd " + pandocFilePath + projectName + " & mkdir " + packageName);

            String mediawikiFilePath = outPutFilePath + "\\" + allclasses + ".mediawiki";
            String pandocCmd = "pandoc -f html -t mediawiki " + allClassesFilePath + " -o " + mediawikiFilePath;
            run("cd " + pandocFilePath + " & " + pandocCmd);

            String packageMainFiles = sourcepath+"\\"+ packageName+ "\\com\\trend4web\\adscampaigns";
            List<String> subpackages = getSubDirectories(packageMainFiles);

            for (String subpackage : subpackages) {
                String packageFilePath = packageMainFiles +"\\"+ subpackage;
                Set<String> fileSet = listFilesUsingJavaIO(packageFilePath);

                List<String> excludedFiles = new ArrayList<>();
                excludedFiles.add("package-tree.html");
                excludedFiles.add("package-summary.html");
                excludedFiles.add("package-use.html");

                for (String file : fileSet) {
                    if (!excludedFiles.contains(file)) {
                        String fileNameWithoutHtml = file.split("\\.")[0];
                        String htmlFilePath = packageFilePath+"\\"+file;
                        mediawikiFilePath = packageName+ "\\"+fileNameWithoutHtml + ".mediawiki";
                        if(new File(mediawikiFilePath).exists()){
                            mediawikiFilePath = projectName + "\\"+fileNameWithoutHtml+"_1" + ".mediawiki";
                        }
                        String fullPathToMediaWikiFile = outputpath +"\\"+ mediawikiFilePath;
                        pandocCmd = "pandoc -f html -t mediawiki " + htmlFilePath + " -o " + fullPathToMediaWikiFile;
                        run("cd " + pandocFilePath + " & " + pandocCmd);
                        Thread.sleep(500);
                        removeLines(fullPathToMediaWikiFile);
                    }
                }
            }

        }

        System.out.println("==================Successfully Finished==================");

    }

    public static List<String> getSubDirectories(String filePath){
        File file = new File(filePath);
        String[] directories = file.list((current, name) -> new File(current, name).isDirectory());
        return List.of(directories);
    }

    public static void removeLines(String filePath){
        boolean topBar = false;
        String topDiv = "<div role=\"main\">";
        String bottomDiv = "<div class=\"bottomNav\">";
        try(Scanner scanner = new Scanner(new File(filePath))) {
            StringBuilder stringBuilder = new StringBuilder();
            while (scanner.hasNextLine() && !topBar){
                String line = scanner.nextLine();
                if(line.contains(topDiv)){
                    stringBuilder.append(line).append('\n');
                    topBar = true;
                }
            }
            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                 if(line.contains(bottomDiv)){
                     break;
                 }
                stringBuilder.append(line).append('\n');
            }
            FileWriter fileWriter = new FileWriter(filePath);
            fileWriter.write(stringBuilder.toString());
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Set<String> listFilesUsingJavaIO(String dir) {
        return Stream.of(new File(dir).listFiles())
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toSet());
    }

    public static void run(String command) throws IOException {
        Runtime rt = Runtime.getRuntime();
        Process proc = rt.exec(CMD_PREFIX+command);
        if(commandDebug) {
            System.out.println("======================COMMAND======================");
            System.out.println(command);
            System.out.println("===================================================");
        }
        BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(proc.getInputStream()));

        BufferedReader stdError = new BufferedReader(new
                InputStreamReader(proc.getErrorStream()));

        // Read the output from the command
        if(commandDebug) {
            System.out.println("--------------------------COMMAND OUTPUT--------------------------\n");
            String s = null;
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }
            System.out.println("--------------------------OUTPUT END--------------------------\n");

            // Read any errors from the attempted command
            System.out.println("--------------------------COMMAND ERROR--------------------------\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }
            System.out.println("--------------------------ERROR END--------------------------\n");
        }
    }

}