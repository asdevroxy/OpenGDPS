package com.fractalmines.plugin;

public @interface Plugin {
    String id();
    String displayName();
    String version() default "1.0.0";

    String[] authors() default {};
    String[] dependencies() default {};
}
