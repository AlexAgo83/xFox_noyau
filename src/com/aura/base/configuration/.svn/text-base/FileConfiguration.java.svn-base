package com.aura.base.configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public abstract class FileConfiguration extends MemoryConfiguration {
    public FileConfiguration() {
        super();
    }

    public FileConfiguration(Configuration defaults) {
        super(defaults);
    }

    public void save(File file) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null");
        }
        String data = saveToString();
        FileWriter writer = new FileWriter(file);
        try {
            writer.write(data);
        } finally {
            writer.close();
        }
    }

    public void save(String file) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null");
        }

        save(new File(file));
    }

    public abstract String saveToString();
    public void load(File file) throws FileNotFoundException, IOException, InvalidConfigurationException {
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null");
        }

        load(new FileInputStream(file));
    }
    public void load(InputStream stream) throws IOException, InvalidConfigurationException {
        if (stream == null) {
            throw new IllegalArgumentException("Stream cannot be null");
        }

        InputStreamReader reader = new InputStreamReader(stream);
        StringBuilder builder = new StringBuilder();
        BufferedReader input = new BufferedReader(reader);

        try {
            String line;

            while ((line = input.readLine()) != null) {
                builder.append(line);
                builder.append('\n');
            }
        } finally {
            input.close();
        }

        loadFromString(builder.toString());
    }

    public void load(String file) throws FileNotFoundException, IOException, InvalidConfigurationException {
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null");
        }

        load(new File(file));
    }

    public abstract void loadFromString(String contents) throws InvalidConfigurationException;

    protected abstract String buildHeader();

    @Override
    public FileConfigurationOptions options() {
        if (options == null) {
            options = new FileConfigurationOptions(this);
        }

        return (FileConfigurationOptions) options;
    }
}