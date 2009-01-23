/*
 *   Command line application demonstration for exe4j
 */


import java.io.*;
import java.util.Properties;

public class HelloCli {

    private static String getGreetingText() {

        String greetingName;
        try {
            File propertiesFile = new File(System.getProperty("user.home"), "hello.properties");
            Properties properties = new Properties();
            FileInputStream inputStream = new FileInputStream(propertiesFile);
            properties.load(inputStream);
            inputStream.close();
            greetingName = properties.getProperty("greetingName");

        } catch (IOException ex) {
            greetingName = "world";
        }
        return "Hello " + greetingName + "!";
    }

    public static void main(String[] args) {
        System.out.println(getGreetingText());
    }

}
