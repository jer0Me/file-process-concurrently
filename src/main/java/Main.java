
public class Main {

    public static void main(String[] vars) {
        String filePath = vars[0];
        new EventLogsFileProcessor(4).processEventLogsFile(filePath);
    }
}
