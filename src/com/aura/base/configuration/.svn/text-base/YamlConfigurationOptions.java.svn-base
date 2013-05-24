package com.aura.base.configuration;

public class YamlConfigurationOptions extends FileConfigurationOptions {
    private int indent = 2;

    protected YamlConfigurationOptions(YamlConfiguration configuration) {
        super(configuration);
    }

    @Override
    public YamlConfiguration configuration() {
        return (YamlConfiguration) super.configuration();
    }

    @Override
    public YamlConfigurationOptions copyDefaults(boolean value) {
        super.copyDefaults(value);
        return this;
    }

    @Override
    public YamlConfigurationOptions pathSeparator(char value) {
        super.pathSeparator(value);
        return this;
    }

    @Override
    public YamlConfigurationOptions header(String value) {
        super.header(value);
        return this;
    }

    @Override
    public YamlConfigurationOptions copyHeader(boolean value) {
        super.copyHeader(value);
        return this;
    }

    public int indent() {
        return indent;
    }

    public YamlConfigurationOptions indent(int value) {
        if ((indent < 2) || (value > 9)) {
            throw new IllegalArgumentException("Indent must be between 1 and 10 characters");
        }

        this.indent = value;
        return this;
    }
}
