package by.grechny;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedInputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestTask2 {

    /**
     * Данный метод обрабатывает ввод команды и создает инстанс класса,
     * соответствующего команде, на основе кофигурационного xml-файла
     * @param args не обрабатывается
     * @throws Exception в случае ошибки парсинга xml
     */
    public static void main(String[] args) throws Exception {

        TestTask2 obj = new TestTask2();

        /**
         * Загружаем конфигурационный xml в DOM и передаем в функцию getCommands для парсинга
         */
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(new BufferedInputStream(
                obj.getClass().getClassLoader().getResourceAsStream("testtask2.xml")));
        Map<String, String> commandMap = obj.getCommands(document);

        /**
         * Беспрерывный ввод команд - выход по quit или exit
         * Парсинг аргументов происходит в методе getCommandArgs при необходимости
         */
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("Enter your command: ");
            String command = sc.nextLine().trim();
            Map<String, String> commandArgs = new HashMap<String, String>();
            if (command.contains(" ")) {
                String[] commandSplit = command.split(" ", 2);
                command = commandSplit[0];
                commandArgs = getCommandArgs(commandSplit[1]);
            }
            if (command.equals("exit") || command.equals("quit"))
                return;
            String commandClass = commandMap.get(command);
            try {
                Class CommandClass = Class.forName(commandClass);
                CommandClass.getDeclaredConstructor(Map.class).newInstance(commandArgs);
            } catch (ClassNotFoundException cnfe) {
                System.out.println("Class for this command not found. Please, try again");
            } catch (NullPointerException npe) {
                System.out.print("Command not found. Please, try again. ");
            }
        }
    }

    /**
     * Данный класс парсит полученный DOM
     * @param document DOM для парсинга
     * @return Map c ключем "имя команды" и значением "класс команды"
     */
    private Map<String,String> getCommands(Document document) {

        Map<String, String> commandMap = new HashMap<String, String>();
        String commandName = null, commandClass = null;

        Node root = document.getDocumentElement();
        NodeList commands = root.getChildNodes();

        for (int i = 0; i < commands.getLength(); i++) {
            Node command = commands.item(i);
            // Если нода не текст, то это описание команды - заходим внутрь
            if (command.getNodeType() != Node.TEXT_NODE) {
                NodeList commandsProps = command.getChildNodes();
                for (int j = 0; j < commandsProps.getLength(); j++) {
                    Node commandProp = commandsProps.item(j);
                    // Если нода не текст, то это один из параметров команды
                    if (commandProp.getNodeType() != Node.TEXT_NODE) {
                        if (commandProp.getNodeName().equals("name")){
                            commandName = commandProp.getChildNodes().item(0).getTextContent();
                        } else if (commandProp.getNodeName().equals("class")){
                            commandClass = commandProp.getChildNodes().item(0).getTextContent();
                        }
                    }
                }
                commandMap.put(commandName, commandClass);
            }
        }
        return commandMap;
    }

    /**
     * Метод для парсинга строки аргументов
     * @param args строка с аргументами
     * @return Map c ключем "имя аргумента" и значением аргумента при наличии
     */
    private static Map<String, String> getCommandArgs(String args) {

        Map<String, String> commandsArgs = new HashMap<String, String>();

        //подготоваливаем regexp
        Pattern pattern = Pattern.compile("((\\S+(=\".*?\"))|(\\S+))");
        Matcher matcher = pattern.matcher(args);

        while (matcher.find()) {
            //берем первую группу, подходящую под regexp
            String string = matcher.group(0);
            //делим полученную строку на сам аргумент и его значение
            String[] stringArray = string.split("=");
            String value = null;
            //если значение не нулевое, срезаем кавычки в начале и конце строки
            if (stringArray.length == 2) {
                value = stringArray[1].replaceFirst("^\"", "");
                value = value.replaceFirst("\"$", "");
            }
            //добавляем в Map аргумент и его значение
            commandsArgs.put(stringArray[0], value);
            //обрезаем строку, чтобы повторить алгоритм для следующей группы
            args = args.substring(matcher.end(0));
            matcher = pattern.matcher(args);
        }

        return commandsArgs;
    }

    /**
     * Метод для проверки, запущено ли приложение под Windows
     * @return true, если запущено под ОС windows
     */
    public static Boolean isWindows(){
        String os = System.getProperty("os.name").toLowerCase();
        return (os.contains("win"));
    }

}

