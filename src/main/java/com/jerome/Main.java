package com.jerome;

public class Main {

    public static void main(String... vars) {
        new EventLogsFileProcessor(
                new EventParametersValidator(vars).getEventParameters()

        ).processEventLogsFile();
    }
}
