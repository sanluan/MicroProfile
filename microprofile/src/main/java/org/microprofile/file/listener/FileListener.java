package org.microprofile.file.listener;

import java.io.File;
import java.io.FileFilter;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.IOCase;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.microprofile.file.event.EventHandler;
import org.microprofile.file.event.FileEvent;
import org.microprofile.file.event.FileEvent.EventType;
import org.microprofile.file.handler.LocalFileAdaptor;

/**
 * FileListener
 * 
 */
public class FileListener implements FileAlterationListener {
    private String basePath;
    private List<EventHandler> eventHandlers;
    private FileAlterationObserver observer;
    private FileAlterationMonitor fileMonitor;
    private LocalFileAdaptor localFileAdaptor;

    /**
     * @param listenPath
     */
    public FileListener(String listenPath) {
        this(new FileAlterationObserver(listenPath));
    }

    /**
     * @param listenPath
     * @param fileFilter
     */
    public FileListener(String listenPath, final FileFilter fileFilter) {
        this(new FileAlterationObserver(listenPath, fileFilter));
    }

    /**
     * @param listenPath
     * @param fileFilter
     * @param caseSensitivity
     */
    public FileListener(String listenPath, final FileFilter fileFilter, final IOCase caseSensitivity) {
        this(new FileAlterationObserver(listenPath, fileFilter, caseSensitivity));
    }

    private FileListener(FileAlterationObserver observer) {
        super();
        this.observer = observer;
        init();
    }

    private void init() {
        this.basePath = observer.getDirectory().getAbsolutePath();
        this.observer.addListener(this);
        this.fileMonitor = new FileAlterationMonitor();
        this.fileMonitor.addObserver(observer);
        this.localFileAdaptor = new LocalFileAdaptor(basePath);
    }

    private void process(EventType eventType, File file) {
        if (null != eventHandlers) {
            String filePath = localFileAdaptor.getRelativeFilePath(file);
            for (EventHandler eventHandler : eventHandlers) {
                eventHandler.handle(new FileEvent(eventType, filePath, file.length()));
            }
        }
    }

    @Override
    public void onFileCreate(File file) {
        process(EventType.FILE_CREATE, file);
    }

    @Override
    public void onFileChange(File file) {
        process(EventType.FILE_MODIFY, file);
    }

    @Override
    public void onFileDelete(File file) {
        process(EventType.FILE_DELETE, file);
    }

    @Override
    public void onDirectoryCreate(File directory) {
        process(EventType.DIRECTORY_CREATE, directory);
    }

    @Override
    public void onDirectoryChange(File directory) {
    }

    @Override
    public void onDirectoryDelete(File directory) {
        process(EventType.DIRECTORY_DELETE, directory);
    }

    @Override
    public void onStart(FileAlterationObserver observer) {

    }

    @Override
    public void onStop(FileAlterationObserver observer) {
    }

    /**
     * @return the localFileAdaptor
     */
    public LocalFileAdaptor getLocalFileAdaptor() {
        return localFileAdaptor;
    }

    /**
     * @param eventHandler
     *            the eventHandler to add
     */
    public void addEventHandler(EventHandler eventHandler) {
        if (null != eventHandler) {
            if (null == this.eventHandlers) {
                this.eventHandlers = new LinkedList<>();
            }
            this.eventHandlers.add(eventHandler);
        }
    }

    /**
     * @throws Exception
     */
    public void start() throws Exception {
        this.fileMonitor.start();
    }

    /**
     * @throws Exception
     */
    public void stop() throws Exception {
        this.fileMonitor.stop();
    }

}
