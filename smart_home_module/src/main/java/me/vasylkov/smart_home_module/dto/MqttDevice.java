package me.vasylkov.smart_home_module.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.vasylkov.smart_home_module.json_deserializer.ExposeDeserializer;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MqttDevice {
    @JsonProperty("friendly_name")
    private String friendlyName;

    @JsonProperty("ieee_address")
    private String ieeeAddress;

    @JsonProperty("interview_completed")
    private Boolean interviewCompleted;

    @JsonProperty("interviewing")
    private Boolean interviewing;

    @JsonProperty("manufacturer")
    private String manufacturer;

    @JsonProperty("model_id")
    private String modelId;

    @JsonProperty("network_address")
    private Integer networkAddress;

    @JsonProperty("power_source")
    private String powerSource;

    @JsonProperty("software_build_id")
    private String softwareBuildId;

    @JsonProperty("supported")
    private Boolean supported;

    @JsonProperty("type")
    private String type;

    @JsonProperty("disabled")
    private Boolean disabled;

    @JsonProperty("definition")
    private Definition definition;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Definition {
        @JsonProperty("description")
        private String description;

        @JsonProperty("exposes")
        private List<Expose> exposes;

        @JsonProperty("model")
        private String model;

        @JsonProperty("options")
        private List<Expose> options;

        @JsonProperty("supports_ota")
        private Boolean supportsOta;

        @JsonProperty("vendor")
        private String vendor;

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Preset {
            @JsonProperty("description")
            private String description;

            @JsonProperty("name")
            private String name;

            @JsonProperty("value")
            private Double value;

        }

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        @JsonDeserialize(using = ExposeDeserializer.class)
        public static abstract class Expose {
            @JsonProperty("type")
            private String type;

            @JsonProperty("description")
            private String description;

            @JsonProperty("name")
            private String name;

            @JsonProperty("label")
            private String label;

            @JsonProperty("property")
            private String property;

            @JsonProperty("is_published")
            private Boolean isPublished;

            @JsonProperty("is_settable")
            private Boolean isSettable;

            @JsonProperty("is_gettable")
            private Boolean isGettable;
        }

        @Data
        @EqualsAndHashCode(callSuper = true)
        @JsonIgnoreProperties(ignoreUnknown = true)
        public abstract static class ValueExpose<T> extends Expose {
            @JsonProperty("current_value")
            private T currentValue;
        }

        @Data
        @EqualsAndHashCode(callSuper = true)
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class BinaryExpose extends ValueExpose<String> {
            @JsonProperty("value_on")
            private String valueOn;

            @JsonProperty("value_off")
            private String valueOff;

            @JsonProperty("value_toggle")
            private String valueToggle;
        }

        @Data
        @EqualsAndHashCode(callSuper = true)
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class NumericExpose extends ValueExpose<Double> {
            @JsonProperty("value_min")
            private Double valueMin;

            @JsonProperty("value_max")
            private Double valueMax;

            @JsonProperty("value_step")
            private Double valueStep;

            @JsonProperty("unit")
            private String unit;

            @JsonProperty("presets")
            private List<Preset> presets;
        }

        @Data
        @EqualsAndHashCode(callSuper = true)
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class EnumExpose extends ValueExpose<String> {
            @JsonProperty("values")
            private List<String> values;
        }

        @Data
        @EqualsAndHashCode(callSuper = true)
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class TextExpose extends ValueExpose<String> {

        }

        @Data
        @EqualsAndHashCode(callSuper = true)
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class CompositeExpose extends Expose {
            @JsonProperty("features")
            private List<Expose> features;
        }

        @Data
        @EqualsAndHashCode(callSuper = true)
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class ListExpose extends Expose {
            @JsonProperty("length_min")
            private Double lengthMin;

            @JsonProperty("length_max")
            private Double lengthMax;

            @JsonProperty("item_type")
            private Expose itemType;
        }

        @Data
        @EqualsAndHashCode(callSuper = true)
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class SpecificExpose extends Expose {
            @JsonProperty("features")
            private List<Expose> features;
        }
    }
}
