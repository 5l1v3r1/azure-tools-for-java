/**
 * Copyright (c) Microsoft Corporation
 * <p/>
 * All rights reserved.
 * <p/>
 * MIT License
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED *AS IS*, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.microsoft.azure.hdinsight.spark.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.microsoft.azure.hdinsight.sdk.rest.IConvertible;
import com.microsoft.azuretools.azurecommons.helpers.NotNull;
import com.microsoft.azuretools.azurecommons.helpers.StringHelper;
import com.microsoft.azuretools.utils.Pair;

import java.util.*;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SparkSubmissionParameter implements IConvertible {
    /**
     * For interactive spark job:
     * <p>
     * kind	            The session kind (required)	session kind
     * proxyUser	        The user to impersonate that will run this session (e.g. bob)	                string
     * jars	            Files to be placed on the java classpath	                                    list of paths
     * pyFiles	            Files to be placed on the PYTHONPATH	                                        list of paths
     * files	            Files to be placed in executor working directory	                            list of paths
     * driverMemory	    Memory for driver (e.g. 1000M, 2G)	                                            string
     * driverCores	        Number of cores used by driver (YARN mode only)	                                int
     * executorMemory	    Memory for executor (e.g. 1000M, 2G)	                                        string
     * executorCores	    Number of cores used by executor	                                            int
     * numExecutors	    Number of executors (YARN mode only)	                                        int
     * archives	        Archives to be uncompressed in the executor working directory (YARN mode only)	list of paths
     * queue	            The YARN queue to submit too (YARN mode only)	string
     * name	            Name of the application	string
     * conf             Spark configuration properties  Map of key=val
     */
    private String file = "";
    private String className = "";

    private String clusterName = "";
    private boolean isLocalArtifact = false;
    private String artifactName = "";
    private String localArtifactPath = "";
    private List<String> files = new ArrayList<>();
    private List<String> jars = new ArrayList<>();
    private List<String> args = new ArrayList<>();
    private Map<String, Object> jobConfig = new HashMap<>();

    public static final String DriverMemory = "driverMemory";
    public static final String DriverMemoryDefaultValue = "4G";

    public static final String DriverCores = "driverCores";
    public static final Integer DriverCoresDefaultValue = 1;

    public static final String ExecutorMemory = "executorMemory";
    public static final String ExecutorMemoryDefaultValue = "4G";

    public static final String NumExecutors = "numExecutors";
    public static final Integer NumExecutorsDefaultValue = 5;

    public static final String ExecutorCores = "executorCores";
    public static final Integer ExecutorCoresDefaultValue = 1;

    public static final String Conf = "conf";   // 	Spark configuration properties

    public static final String NAME = "name";

    public SparkSubmissionParameter() {
    }

    public SparkSubmissionParameter(String clusterName,
                                    boolean isLocalArtifact,
                                    String artifactName,
                                    String localArtifactPath,
                                    String filePath,
                                    String className,
                                    List<String> referencedFiles,
                                    List<String> referencedJars,
                                    List<String> args,
                                    Map<String, Object> jobConfig) {
        this.clusterName = clusterName;
        this.isLocalArtifact = isLocalArtifact;
        this.artifactName = artifactName;
        this.localArtifactPath = localArtifactPath;
        this.file = filePath;
        this.className = className;
        this.files = referencedFiles;
        this.jars = referencedJars;
        this.jobConfig = jobConfig;
        this.args = args;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public void setLocalArtifact(boolean localArtifact) {
        isLocalArtifact = localArtifact;
    }

    public void setArtifactName(String artifactName) {
        this.artifactName = artifactName;
    }

    @JsonIgnore
    public String getClusterName() {
        return clusterName;
    }

    @JsonIgnore
    public boolean isLocalArtifact() {
        return isLocalArtifact;
    }

    @JsonIgnore
    public String getArtifactName() {
        return artifactName;
    }

    @JsonIgnore
    public String getLocalArtifactPath() {
        return localArtifactPath;
    }

    public void setLocalArtifactPath(String path) {
        localArtifactPath = path;
    }

    @JsonProperty("file")
    public String getFile() {
        return file;
    }

    @JsonProperty("className")
    public String getMainClassName() {
        return className;
    }

    @JsonProperty("files")
    public List<String> getReferencedFiles() {
        return files;
    }

    @JsonProperty("jars")
    public List<String> getReferencedJars() {
        return jars;
    }

    @JsonProperty("args")
    public List<String> getArgs() {
        return args;
    }

    @JsonIgnore
    public Map<String, Object> getJobConfig() {
        return jobConfig;
    }

    public void setFilePath(String filePath) {
        this.file = filePath;
    }

    @JsonProperty("driverMemory")
    public String getDriverMemory() {
        return (String) jobConfig.get(DriverMemory);
    }

    @JsonProperty("driverCores")
    public Integer getDriverCores() {
        return Optional.ofNullable(jobConfig.get(DriverCores))
                .map(String.class::cast)
                .map(Integer::parseInt)
                .orElse(null);
    }

    @JsonProperty("executorMemory")
    public String getExecutorMemory() {
        return (String) jobConfig.get(ExecutorMemory);
    }

    @JsonProperty("executorCores")
    public Integer getExecutorCores() {
        return Optional.ofNullable(jobConfig.get(ExecutorCores))
                .map(String.class::cast)
                .map(Integer::parseInt)
                .orElse(null);
    }

    @JsonProperty("numExecutors")
    public Integer getNumExecutors() {
        return Optional.ofNullable(jobConfig.get(NumExecutors))
                .map(String.class::cast)
                .map(Integer::parseInt)
                .orElse(null);
    }

    @JsonProperty("conf")
    public Map<String, String> getConf() {
        Map<String, String> jobConf = new HashMap<>();

        Optional.ofNullable(jobConfig.get(Conf))
                .filter(Map.class::isInstance)
                .map(Map.class::cast)
                .ifPresent(conf -> conf.forEach((k, v) -> jobConf.put((String) k, (String) v)));

        return jobConf.isEmpty() ? null : jobConf;
    }

    public static List<SparkSubmissionJobConfigCheckResult> checkJobConfigMap(Map<String, String> jobConfigMap) {

        List<SparkSubmissionJobConfigCheckResult> messageList = new ArrayList<>();
        for (Map.Entry<String, String> entry : jobConfigMap.entrySet()) {
            String entryKey = entry.getKey();
            if (StringHelper.isNullOrWhiteSpace(entryKey)) {
                continue;
            }

            if (entryKey.equals(DriverCores)
                    || entryKey.equals(NumExecutors)
                    || entryKey.equals(ExecutorCores)) {
                if (StringHelper.isNullOrWhiteSpace(entry.getValue())) {
                    messageList.add(new SparkSubmissionJobConfigCheckResult(SparkSubmissionJobConfigCheckStatus.Warning,
                            "Warning : Empty value(s) will be override by default value(s) of system"));
                    continue;
                }

                try {
                    Integer.parseInt(entry.getValue());
                } catch (NumberFormatException e) {
                    messageList.add(new SparkSubmissionJobConfigCheckResult(SparkSubmissionJobConfigCheckStatus.Error,
                            String.format("Error : Failed to parse \"%s\", it should be an integer", entry.getValue())));
                }
            } else if (entryKey.equals(DriverMemory)
                    || entryKey.equals(ExecutorMemory)
                    || entryKey.equals(NAME)) {
                if (StringHelper.isNullOrWhiteSpace(entry.getValue())) {
                    messageList.add(new SparkSubmissionJobConfigCheckResult(SparkSubmissionJobConfigCheckStatus.Warning,
                            "Warning : Empty value(s) will be override by default value(s) of system"));
                }
            }
        }

        return messageList;
    }

    @NotNull
    public Map<String, String> flatJobConfig() {
        Map<String, String> flattedMap = new HashMap<>();

        getJobConfig().forEach((key, value) -> {
            if (isSubmissionParameter(key)) {
                flattedMap.put(key, value == null ? null : value.toString());
            } else if (key.equals(Conf)) {
                new SparkConfigures(value).forEach((scKey, scValue) ->
                        flattedMap.put(scKey, scValue == null ? null : scValue.toString()));
            }
        });

        return flattedMap;
    }

    public void applyFlattedJobConf(Map<String, String> jobConfFlatted) {
        jobConfig.clear();

        SparkConfigures sparkConfig = new SparkConfigures();

        jobConfFlatted.forEach((key, value) -> {
            if (isSubmissionParameter(key)) {
                jobConfig.put(key, value);
            } else {
                sparkConfig.put(key, value);
            }
        });

        if (!sparkConfig.isEmpty()) {
            jobConfig.put(Conf, sparkConfig);
        }
    }

    public String serializeToJson() {
        return convertToJson().orElse("");
    }

    public static final String[] parameterList = new String[]{SparkSubmissionParameter.DriverMemory, SparkSubmissionParameter.DriverCores,
            SparkSubmissionParameter.ExecutorMemory, SparkSubmissionParameter.ExecutorCores, SparkSubmissionParameter.NumExecutors};

    //the first value in pair should be in same order with parameterList
    public static final Pair<String, Object>[] defaultParameters = new Pair[]{
            new Pair<>(SparkSubmissionParameter.DriverMemory, SparkSubmissionParameter.DriverMemoryDefaultValue),
            new Pair<>(SparkSubmissionParameter.DriverCores, SparkSubmissionParameter.DriverCoresDefaultValue),
            new Pair<>(SparkSubmissionParameter.ExecutorMemory, SparkSubmissionParameter.ExecutorMemoryDefaultValue),
            new Pair<>(SparkSubmissionParameter.ExecutorCores, SparkSubmissionParameter.ExecutorCoresDefaultValue),
            new Pair<>(SparkSubmissionParameter.NumExecutors, SparkSubmissionParameter.NumExecutorsDefaultValue)
    };

    /**
     * Checks whether the key is one of Spark Job submission parameters or not
     *
     * @param key the key string to check
     * @return true if the key is a member of submission parameters; false otherwise
     */
    public static boolean isSubmissionParameter(String key) {
        return Arrays.stream(SparkSubmissionParameter.parameterList).anyMatch(key::equals);
    }
}