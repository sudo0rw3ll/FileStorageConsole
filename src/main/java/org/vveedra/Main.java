package org.vveedra;

import baluni.filestorage.MyFileStorage;
import baluni.filestorage.StorageConfig;
import baluni.filestorage.StorageManager;
import baluni.model.Fajl;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    static MyFileStorage storage;
    static StorageConfig config;
    static String storageType;
    static List<Fajl> cache;

    public static void main(String[] args) throws Exception {
        if(args.length != 2)
            throw new Exception("[-] Please enter valid number of arguments 2");
        storage = makeStorage(args);

        System.out.println("Current config\n"+storage.getStorageConfig().getStorageName() + "\n" + storage.getStorageConfig().getDefaultStorageSize() + "\n" + storage.getStorageConfig().getForbiddenExtensions());
        getInput();
    }

    private static MyFileStorage makeStorage(String[] args) throws ClassNotFoundException {
        String storageName = args[0];
        String storagePath = args[1];
        String classForName = "";
        cache = new ArrayList<>();

        MyFileStorage storage = null;

        if(storageName.equalsIgnoreCase("google_drive")){
            classForName = "main.GoogleFileStorage";
            storageType = "google";
        }

        if(storageName.equalsIgnoreCase("local_storage")){
            classForName = "baluni.implementation.LocalStorage";
            storageType = "local";
        }

        Class.forName(classForName);
        return StorageManager.makeStorage(storagePath);
    }

    private static void getInput(){
        printBanner();
        Scanner scanner = new Scanner(System.in);

        String line = "";

        do{
            System.out.print("["+storageType+"] # ");
            line = scanner.nextLine();

            String[] data = line.split(" ");

            String command = data[0];

            switch (command){
                case "help":
                    printHelp();
                    break;
                case "chStorageName":
                    if(data.length > 1){
                        String name = data[1];

                        if(!name.isEmpty())
                            storage.setStorageName(name);
                        else
                            System.out.println("Please provide non-empty name");
                    }
                    System.out.println("New storage name -> " + storage.getStorageConfig().getStorageName());
                    break;
                case "chStorageSize":
                    if(data.length > 1){
                        long newSize = 0;

                        try{
                            newSize = Long.parseLong(data[1]);
                        }catch (Exception e){
                            System.out.println("Cannot parse new storage size");
                        }

                        storage.setStorageSize(newSize);
                        System.out.println("New storage size -> " + storage.getStorageConfig().getDefaultStorageSize());
                    }
                    break;
                case "chForbiddenExtensions":
                    if(data.length > 1){
                        String[] extensions = data[1].split(",");

                        for(String extension : extensions){
                            if(!storage.getStorageConfig().getForbiddenExtensions().contains(extension))
                                storage.getStorageConfig().getForbiddenExtensions().add(extension);
                        }

                        System.out.println("Forbidden extensions -> " + storage.getStorageConfig().getForbiddenExtensions());
                    }
                    break;
                case "config":
                case "exit":
                    storage.saveStorageConfig(storage.getSotragePath());
                    break;
                case "mkdir":
                    if(data.length > 1){
                        String path = data[1];
                        String dirName = data[2];

                        storage.createDir(path, dirName);
                    }
                    break;
                case "mkDirCapacity":
                    if(data.length == 4){
                        String path = data[1];
                        String dirName = data[2];
                        int capacity = 0;

                        try{
                            capacity = Integer.parseInt(data[3]);
                        }catch (Exception e){
                            System.out.println("Cannot parse capacity value");
                        }

                        storage.createDirectory(path, dirName, capacity);
                    }

                    break;

                case "mkDirMult":
                    if(data.length == 3){
                        String path = data[1];
                        String pattern = data[2];

                        storage.createDirectories(path, pattern);
                    }

                    break;
                case "moveFiles":
                    if(data.length == 3){
                        String sourcePath = data[1];
                        String destinationPath = data[2];

                        storage.moveFiles(sourcePath, destinationPath);
                    }
                    break;
                case "moveFile":
                    if(data.length == 3){
                        String sourcePath = data[1];
                        String destination = data[2];

                        storage.moveFile(sourcePath, destination);
                    }

                    break;

                case "download":
                    if(data.length == 3){
                        String source = data[1];
                        String dest = data[2];

                        storage.download(source, dest);
                    }

                    break;

                case "rename":
                    if(data.length == 3){
                        String oldFileName = data[1];
                        String newFileName = data[2];

                        storage.rename(oldFileName, newFileName);
                    }

                    break;
                case "listFilesInDir":
                    if(data.length == 2){
                        String dirPath = data[1];
                        cache = storage.listFilesInDir(dirPath);

                        for(Fajl fajl : cache){
                            System.out.println(fajl);
                        }
                    }

                    break;
                case "listFilesInSubDir":
                    if(data.length == 2){
                        String dirPath = data[1];
                        cache = storage.listFilesInSubDir(dirPath);

                        for(Fajl fajl : cache){
                            System.out.println(fajl);
                        }
                    }

                    break;
                case "listFiles":
                    if(data.length == 2){
                        String dirPath = data[1];
                        cache = storage.listFiles(dirPath);

                        for(Fajl fajl : cache){
                            System.out.println(fajl);
                        }
                    }
                    break;
                case "listFilesForExtension":
                    if(data.length == 2){
                        String extension = data[1];

                        cache = storage.listFilesForExtension(extension);

                        for(Fajl fajl : cache){
                            System.out.println(fajl);
                        }
                    }


                    if(data.length == 3){
                        String dirName = data[1];
                        String extension = data[2];

                        cache = storage.listFilesForExtension(dirName, extension);

                        for(Fajl fajl : cache){
                            System.out.println(fajl);
                        }
                    }
                    break;
                case "listFilesForName":
                    if(data.length == 2){
                        String name = data[1];

                        cache = storage.listFilesForName(name);

                        for(Fajl fajl : cache){
                            System.out.println(fajl);
                        }
                    }

                    if(data.length == 3){
                        String dirName = data[1];
                        String name = data[2];

                        cache = storage.listFilesForName(dirName, name);

                        for(Fajl fajl : cache){
                            System.out.println(fajl);
                        }
                    }

                    break;

                case "listDirForNames":
                    if(data.length == 3){
                        String dirName = data[1];
                        String[] names = data[2].split(",");

                        List<String> lookupNames = List.of(names);

                        if(storage.listDirForNames(dirName, lookupNames)){
                            System.out.println("Directory contains provided names");
                        }else{
                            System.out.println("Directory does not contain provided names");
                        }
                    }

                    break;
                case "listFileByDate":
                    if(data.length == 3){
                        String date = data[1];
                        String dir = data[2];

                        cache = storage.listFileByDate(date, dir);

                        for(Fajl fajl : cache){
                            System.out.println(fajl);
                        }
                    }

                    break;

                case "listFilesBetweenDates":
                    if(data.length == 4){
                        String startDate = data[1];
                        String endDate = data[2];
                        String dir = data[3];

                        cache = storage.listFilesBetweenDates(startDate, endDate, dir);

                        for(Fajl fajl : cache){
                            System.out.println(fajl);
                        }
                    }

                    break;
                case "findDirOfFile":
                    if(data.length == 2){
                        String fileName = data[1];

                        Fajl result = storage.findDirectoryOfFile(fileName);

                        if(result != null){
                            System.out.println(result);
                        }
                    }

                    break;
                case "sort":
                    if(data.length == 5){
                        boolean byName = data[1].equalsIgnoreCase("true");
                        boolean byCreationDate = data[2].equalsIgnoreCase("true");
                        boolean byModificationDate = data[3].equalsIgnoreCase("true");
                        boolean asc = data[4].equalsIgnoreCase("true");

                        cache = storage.sort(cache, byName, byCreationDate, byModificationDate, asc);

                        for(Fajl fajl : cache){
                            System.out.println(fajl);
                        }
                    }

                    break;
                case "filter":
                    if(data.length == 7){
                        boolean path = data[1].equalsIgnoreCase("true");
                        boolean name = data[2].equalsIgnoreCase("true");
                        boolean size = data[3].equalsIgnoreCase("true");
                        boolean creation = data[4].equalsIgnoreCase("true");
                        boolean modification = data[5].equalsIgnoreCase("true");
                        boolean extension = data[6].equalsIgnoreCase("true");

                        List<Fajl> temp = storage.filterData(cache, path, name, size, creation, modification, extension);

                        for(Fajl fajl : temp){
                            System.out.println(fajl);
                        }
                    }
                    break;

                case "mkfile":
                    if(data.length == 3){
                        String dirName = data[1];
                        String fileName = data[2];

                        storage.createFile(dirName, fileName);
                    }

                    break;
                case "deleteDir":
                    if(data.length == 2){
                        String[] deletionData = data[1].split(",");

                        List<Fajl> zaBrisanje = new ArrayList<>();

                        for(String deletionItem : deletionData){
                            Fajl fajl = new Fajl("","","");

                            if(storageType.equalsIgnoreCase("google")) {
                                fajl.setPath(deletionItem);
                            }

                            if(storageType.equalsIgnoreCase("local")) {
                                fajl.setPath(storage.getSotragePath() + "\\" + deletionItem);
                            }

                            if(!zaBrisanje.contains(fajl)) {
                                zaBrisanje.add(fajl);
                            }
                        }

                        storage.deleteDirectories(zaBrisanje);
                    }
                    break;
                case "deleteFiles":
                    if(data.length == 2){
                        String[] deletionData = data[1].split(",");

                        List<Fajl> zaBrisanje = new ArrayList<>();

                        for(String deletionItem : deletionData){
                            Fajl fajl = new Fajl("","","");

                            if(storageType.equalsIgnoreCase("google"))
                                fajl.setPath(deletionItem);

                            if(storageType.equalsIgnoreCase("local"))
                                fajl.setPath(storage.getSotragePath() + "\\" + deletionItem);

                            if(!zaBrisanje.contains(fajl))
                                zaBrisanje.add(fajl);
                        }

                        storage.deleteFiles(zaBrisanje);
                    }

                    break;
                case "upload":
                    if(data.length == 3){
                        String folderId = data[1];
                        String[] filePaths = data[2].split(",");

                        List<Fajl> zaUpload = new ArrayList<>();

                        for(String filePath : filePaths){
                            File file = new File(filePath);
                            String name = "";
                            String extension = "";

                            if(file.getName().contains(".")){
                                name = file.getName().substring(0, file.getName().indexOf("."));
                                extension = file.getName().substring(file.getName().indexOf(".")+1,file.getName().length());
                            }else{
                                name = file.getName();
                                extension = "";
                            }

                            Fajl fajl = new Fajl(name, extension, filePath);

                            if(!zaUpload.contains(fajl))
                                zaUpload.add(fajl);
                        }

                        storage.fileUpload(folderId,zaUpload);
                    }

                    break;
                default:
                    System.out.println("Bad command");
                    break;
            }

        }while(!line.equalsIgnoreCase("exit"));
    }

    private static void printBanner(){
        System.out.println("/ $$$$$$$$ /$$ /$$            /$$$$$$   /$$                                                       /$$$$$$            /$$");
        System.out.println("| $$_____/|__/| $$           /$$__  $$ | $$                                                      /$$__  $$          |__/");
        System.out.println("| $$       /$$| $$  /$$$$$$ | $$  \\__//$$$$$$    /$$$$$$   /$$$$$$  /$$$$$$   /$$$$$$   /$$$$$$ | $$  \\ $$  /$$$$$$  /$$");
        System.out.println("| $$$$$   | $$| $$ /$$__  $$|  $$$$$$|_  $$_/   /$$__  $$ /$$__  $$|____  $$ /$$__  $$ /$$__  $$| $$$$$$$$ /$$__  $$| $$");
        System.out.println("| $$__/   | $$| $$| $$$$$$$$ \\____  $$ | $$    | $$  \\ $$| $$  \\__/ /$$$$$$$| $$  \\ $$| $$$$$$$$| $$__  $$| $$  \\ $$| $$");
        System.out.println("| $$      | $$| $$| $$_____/ /$$  \\ $$ | $$ /$$| $$  | $$| $$      /$$__  $$| $$  | $$| $$_____/| $$  | $$| $$  | $$| $$");
        System.out.println("| $$      | $$| $$|  $$$$$$$|  $$$$$$/ |  $$$$/|  $$$$$$/| $$     |  $$$$$$$|  $$$$$$$|  $$$$$$$| $$  | $$| $$$$$$$/| $$");
        System.out.println("|__/      |__/|__/ \\_______/ \\______/   \\___/   \\______/ |__/      \\_______/ \\____  $$ \\_______/|__/  |__/| $$____/ |__/");
        System.out.println("                                                                             /$$  \\ $$                    | $$          ");
        System.out.println("                                                                            |  $$$$$$/                    | $$          ");
        System.out.println("                                                                             \\______/                     |__/          ");
    }

    private static void printHelp(){
        if(storageType.equalsIgnoreCase("local")) {
            System.out.println("===============LocalStorage Commands===============");
            System.out.println("help - List of all available commands in console");
            System.out.println("exit - Terminates current session");
            System.out.println("=======Storage Config Commands=======");
            System.out.println("chStorageName <storageName> - Sets storage name");
            System.out.println("chStorageSize <storageSize> - Sets storage size");
            System.out.println("chForbiddenExtensions forbidden1,forbidden2,... - Adds forbidden extensions to storage config");
            System.out.println("config - Saves config changes");
            System.out.println("=======Directory Commands=======");
            System.out.println("mkdir <path> <dirName> - Creates directory with given name on specified path");
            System.out.println("mkdirCapacity <path> <dirName> <capacity> - Creates directory with given name on specified path with specified capacity");
            System.out.println("mkdirMult <path> <pattern> Pattern - f{1..12},f{12->1},f[1:3] - Creates directories on a given path using specified pattern");
            System.out.println("deleteDir file1,file2.. - Deletes specified directories from storage");
            System.out.println("moveFiles <dirSource> <destination> - Moves directory content from one directory to another");
            System.out.println("=======File Commands=======");
            System.out.println("mkfile <path> <fileName> - Creates file on specified path with a given filename");
            System.out.println("deleteFiles file1,file2.. - Deletes specified files from storage");
            System.out.println("moveFiles <fileSource> <destination> - Moves single file from one destination to another");
            System.out.println("download <sourcePath> <destination> - Downloads file from source to specified destination");
            System.out.println("rename <oldFileName> <newFileName> - Changes old filename to specified new file name");
            System.out.println("=======List/Search Commands=======");
            System.out.println("listFilesInDir <dirName> - Lists files in specified dir");
            System.out.println("listFilesInSubDir <dirName> - Lists files in specified dir and all it's subdirectories");
            System.out.println("listFiles <dirName> - Lists files in a specified dir");
            System.out.println("listFilesForExtension <extension> - Lists files with specified extension in whole storage");
            System.out.println("listFilesForExtension <dirName> <extension> - Lists files with specified extension in specified directory");
            System.out.println("listFilesForName <name> - Lists files with specified name in whole storage");
            System.out.println("listFilesForName <dir> <name> - Lists files with specified name in specified directory");
            System.out.println("listDirForNames <dir> name1,name2,... - Checks if specified directory contains specified list of names");
            System.out.println("listFileByDate <date> <dir> - Date format YYYY-MM-DD - Lists files created/modified on a specified date in given directory");
            System.out.println("listFilesBetweenDates <startDate> <endDate> <dirPath> - Date format YYYY-MM-DD - Lists files created/modified between specified dates in given directory");
            System.out.println("findFileInDir <dir> <fileName> - Searches for a file with specified filename in a specified directory");
            System.out.println("findDirOfFile <fileName> - Searches for a parent directory of a specified file");
            System.out.println("=======Sort/Filter=======");
            System.out.println("sort <true/false>... (4 args) - Sorts listing data by 4 flags, name, creation date, modification date, asc/desc");
            System.out.println("filterData <true/false>... (6 args) - Filters output by 6 flags, name, extension, path, creation date, modification date, file size");
        }

        if(storageType.equalsIgnoreCase("google")){
            System.out.println("===============GoogleStorage Commands===============");
            System.out.println("help - List of all available commands in console");
            System.out.println("exit - Terminates current session");
            System.out.println("=======Storage Config Commands=======");
            System.out.println("chStorageName <storageName> - Sets storage name");
            System.out.println("chStorageSize <storageSize> - Sets storage size");
            System.out.println("chForbiddenExtensions forbidden1,forbidden2,... - Adds forbidden extensions to storage config");
            System.out.println("config - Saves config changes");
            System.out.println("=======Directory Commands=======");
            System.out.println("mkdir <folderId> <dirName> - Creates directory with given name on specified path");
            System.out.println("mkdirCapacity <folderId> <dirName> <capacity> - Creates directory with given name on specified path with specified capacity");
            System.out.println("mkdirMult <folderId> <pattern> Pattern - f{1..12},f{12->1},f[1:3] - Creates directories on a given path using specified pattern");
            System.out.println("deleteDir fileID1,fileID2.. - Deletes specified directories from storage");
            System.out.println("moveFiles <dirSourceID> <destinationID> - Moves directory content from one directory to another");
            System.out.println("=======File Commands=======");
            System.out.println("deleteFiles file1ID,file2ID.. - Deletes specified files from storage");
            System.out.println("moveFile <fileSourceID> <destinationID> - Moves single file from one destination to another");
            System.out.println("download <sourcePathID> <destination> - Downloads file from source to specified destination");
            System.out.println("rename <fileID> <newFileName> - Changes old filename to specified new file name");
            System.out.println("upload <folderId> filePath1,filePath2.. - Uploads specified files to specified folder");
            System.out.println("=======List/Search Commands=======");
            System.out.println("listFilesInDir <dirID> - Lists files in specified dir");
            System.out.println("listFilesInSubDir <dirID> - Lists files in specified dir and all it's subdirectories");
            System.out.println("listFiles <dirID> - Lists files in a specified dir");
            System.out.println("listFilesForExtension <extension> - Lists files with specified extension in whole storage");
            System.out.println("listFilesForExtension <dirID> <extension> - Lists files with specified extension in specified directory");
            System.out.println("listFilesForName <name> - Lists files with specified name in whole storage");
            System.out.println("listFilesForName <dirID> <name> - Lists files with specified name in specified directory");
            System.out.println("listDirForNames <dirID> name1,name2,... - Checks if specified directory contains specified list of names");
            System.out.println("listFileByDate <date> <dirID> - Date format YYYY-MM-DD - Lists files created/modified on a specified date in given directory");
            System.out.println("listFilesBetweenDates <startDate> <endDate> <dirID> - Date format YYYY-MM-DD - Lists files created/modified between specified dates in given directory");
            System.out.println("findFileInDir <dirID> <fileName> - Searches for a file with specified filename in a specified directory");
            System.out.println("findDirOfFile <fileName> - Searches for a parent directory of a specified file");
            System.out.println("=======Sort/Filter=======");
            System.out.println("sort <true/false>... (4 args) - Sorts listing data by 4 flags, name, creation date, modification date, asc/desc");
            System.out.println("filterData <true/false>... (6 args) - Filters output by 6 flags, name, extension, path, creation date, modification date, file size");
        }
    }

}
