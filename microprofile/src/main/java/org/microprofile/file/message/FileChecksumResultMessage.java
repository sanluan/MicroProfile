package org.microprofile.file.message;

import java.util.List;

public class FileChecksumResultMessage {
    private List<FileChecksum> fileList;

    /**
     * @return the fileList
     */
    public List<FileChecksum> getFileList() {
        return fileList;
    }

    /**
     * @param fileList
     *            the fileList to set
     */
    public void setFileList(List<FileChecksum> fileList) {
        this.fileList = fileList;
    }

    public static class FileChecksum {
        private String filePath;
        private boolean directory;
        private long fileSize;
        private byte[] checksum;

        /**
         * @return the filePath
         */
        public String getFilePath() {
            return filePath;
        }

        /**
         * @param filePath
         *            the filePath to set
         */
        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        /**
         * @return the directory
         */
        public boolean isDirectory() {
            return directory;
        }

        /**
         * @param directory
         *            the directory to set
         */
        public void setDirectory(boolean directory) {
            this.directory = directory;
        }

        /**
         * @return the fileSize
         */
        public long getFileSize() {
            return fileSize;
        }

        /**
         * @param fileSize
         *            the fileSize to set
         */
        public void setFileSize(long fileSize) {
            this.fileSize = fileSize;
        }

        /**
         * @return the checksum
         */
        public byte[] getChecksum() {
            return checksum;
        }

        /**
         * @param checksum
         *            the checksum to set
         */
        public void setChecksum(byte[] checksum) {
            this.checksum = checksum;
        }
    }

}