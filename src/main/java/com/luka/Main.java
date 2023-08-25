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

    public static void main(String[] args) throws IOException {
        String projectName = "Javapandoc";
        String pandocFilePath = "C:\\Users\\USER.LUKA\\AppData\\Local\\Pandoc\\";
        String outputpath = pandocFilePath+projectName;
        String sourcepath  = "C:\\Users\\USER.LUKA\\IdeaProjects\\Javapandoc\\src\\main\\java";
//        String sourcepath  = "C:\\Users\\USER.LUKA\\IdeaProjects\\ads-campaigns-desk-proxy\\ads-campaigns-config\\src\\main\\java\\com\\trend4web\\adscampaigns\\config";
        String subpackages = "calc";
        String command = "javadoc -d "+outputpath+" -sourcepath "+sourcepath+" -subpackages "+subpackages;
        run(command);
        String fileName = "allclasses";
        String htmlFilePath = projectName+"\\"+fileName+".html";
        String mediaWikiDirectoryName = "mediawiki";
        run("cd "+pandocFilePath+projectName+" & mkdir "+mediaWikiDirectoryName);

        String mediawikiFilePath = projectName+"\\"+mediaWikiDirectoryName+"\\"+fileName+".mediawiki";
        String pandocCmd = "pandoc -f html -t mediawiki "+htmlFilePath+" -o "+mediawikiFilePath;
        System.out.println();
        run("cd "+pandocFilePath+" & "+pandocCmd);


        Set<String> fileSet = listFilesUsingJavaIO(outputpath+"\\"+subpackages);
        List<String> excludedFiles = new ArrayList<>();
        excludedFiles.add("package-tree.html");
        excludedFiles.add("package-summary.html");
        for(String file: fileSet){
            if(!excludedFiles.contains(file)){
                String fileNameWithoutHtml = file.split("\\.")[0];
                htmlFilePath = projectName+"\\"+subpackages+"\\"+file;
                mediawikiFilePath = projectName+"\\"+mediaWikiDirectoryName+"\\"+fileNameWithoutHtml+".mediawiki";
                pandocCmd = "pandoc -f html -t mediawiki "+htmlFilePath+" -o "+mediawikiFilePath;
                run("cd "+pandocFilePath+" & "+pandocCmd);
                removeLines(pandocFilePath+mediawikiFilePath);
            }
        }

    }

    public static void removeLines(String filePath){
        boolean topBar = false;
        String topDiv = "<div role=\"main\">";
        String bottomDiv = "<div class=\"bottomNav\">";
        System.out.println("filePath = " + filePath);
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
        System.out.println("======================COMMAND======================");
        System.out.println(command);
        System.out.println("===================================================");
        BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(proc.getInputStream()));

        BufferedReader stdError = new BufferedReader(new
                InputStreamReader(proc.getErrorStream()));

        // Read the output from the command
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
//
//
//
//    public static void runCmd(String cmd) throws IOException {
//        Process p = Runtime.
//                getRuntime().
//                exec(CMD_PREFIX+cmd);
//        BufferedReader stdInput = new BufferedReader(
//                new InputStreamReader( p.getInputStream() ));
//
//        String s ;
//        while ((s = stdInput.readLine()) != null) {
//            System.out.println(s);
//        }
//    }
}