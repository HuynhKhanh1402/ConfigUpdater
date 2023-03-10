/*
 * Copyright (c) 2023 Huỳnh Quốc Khánh
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.khanh.libs.configupdater;

import com.google.common.base.Preconditions;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class ConfigUpdater {
    /**
     * Method MemorySection.class#setComments(java.lang.String,java.util.List)
     */
    private static Method setCommentMethod;

    /**
     * Method MemorySection.class#getComments(java.lang.String)
     */
    private static Method getCommentMethod;

    /**
     * Update Yaml file
     * @param file file you want to update
     * @param resource InputStream of file
     * @param ignoredSections a list of ignore update sections
     * @throws IOException save file error
     * @throws IllegalArgumentException file is not exists
     */
    public static void update(@NotNull File file, @NotNull InputStream resource, @Nullable List<String> ignoredSections) throws IOException {
        Preconditions.checkArgument(file.exists(), "File doesn't exists.");

        boolean supportComment = isSupportComment();

        if (ignoredSections == null){
            ignoredSections = new ArrayList<>();
        }

        Reader reader = new InputStreamReader(resource);

        FileConfiguration defaultConfig = YamlConfiguration.loadConfiguration(reader);

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        for (String section: defaultConfig.getKeys(true)){

            if (isIgnoredSection(section, ignoredSections)){
                continue;
            }

            if (!config.contains(section)){

                config.set(section, defaultConfig.get(section));

                if (supportComment){
                    try {
                        setCommentMethod.invoke(config, section, getCommentMethod.invoke(defaultConfig, section));
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        config.save(file);
    }

    private static boolean isIgnoredSection(String section, List<String> ignoredSections){
        for (String ignoredSection: ignoredSections){
            if (section.startsWith(ignoredSection)){
                return true;
            }
        }
        return false;
    }


    @SuppressWarnings("JavaReflectionMemberAccess")
    private static boolean isSupportComment(){
        try {
            setCommentMethod = MemorySection.class.getMethod("setComments", String.class, List.class);
            getCommentMethod = MemorySection.class.getMethod("getComments", String.class);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }
}
