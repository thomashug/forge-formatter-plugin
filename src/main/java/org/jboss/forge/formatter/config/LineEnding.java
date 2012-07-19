package org.jboss.forge.formatter.config;

public enum LineEnding {

    /**
     * Line ending of current system
     */
    AUTO {
        @Override
        public String lineEndings(String fileContent) {
            return null;
        }
    },

    /**
     * Keep line ending of the file
     */
    KEEP {
        @Override
        public String lineEndings(String fileContent) {
            return determineLineEnding(fileContent);
        }

        private String determineLineEnding(String fileContent) {
            int lfCount = 0;
            int crCount = 0;
            int crlfCount = 0;

            for (int i = 0; i < fileContent.length(); i++) {
                char c = fileContent.charAt(i);
                if (c == '\r') {
                    if ((i + 1) < fileContent.length() && fileContent.charAt(i + 1) == '\n') {
                        crlfCount++;
                        i++;
                    } else {
                        crCount++;
                    }
                } else if (c == '\n') {
                    lfCount++;
                }
            }
            if (lfCount > crCount && lfCount > crlfCount) {
                return LINE_ENDING_LF_CHAR;
            } else if (crlfCount > lfCount && crlfCount > crCount) {
                return LINE_ENDING_CRLF_CHARS;
            } else if (crCount > lfCount && crCount > crlfCount) {
                return LINE_ENDING_CR_CHAR;
            }
            return null;
        }
    },

    /**
     * Unix/Mac line endings
     */
    LF {
        @Override
        public String lineEndings(String fileContent) {
            return LINE_ENDING_LF_CHAR;
        }
    },

    /**
     * DOS/Windows line endings
     */
    CRLF {
        @Override
        public String lineEndings(String fileContent) {
            return LINE_ENDING_CRLF_CHARS;
        }
    },

    /**
     * Early Mac line endings
     */
    CR {
        @Override
        public String lineEndings(String fileContent) {
            return LINE_ENDING_CR_CHAR;
        }
    };

    private static final String LINE_ENDING_LF_CHAR = "\n";
    private static final String LINE_ENDING_CRLF_CHARS = "\r\n";
    private static final String LINE_ENDING_CR_CHAR = "\r";

    public abstract String lineEndings(String fileContent);

}
