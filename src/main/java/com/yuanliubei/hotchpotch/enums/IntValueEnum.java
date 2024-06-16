package com.yuanliubei.hotchpotch.enums;

import lombok.NonNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author yuanlb
 * @since 2023/4/3
 */
public interface IntValueEnum {

    int getIntValue();

    String getTitle();

    default Class<?> getDetailClass() {
        return null;
    }

    default int intValue() {
        return this.getIntValue();
    }

    default boolean equalTo(Integer state) {
        return Objects.equals(state, this.getIntValue());
    }


    static boolean isNotIn(@NonNull Integer value, IntValueEnum... values) {
        return !isIn(value, values);
    }

    static boolean isIn(@NonNull IntValueEnum value, IntValueEnum... values) {
        return isIn(value.intValue(), values);
    }

    static boolean isNotIn(@NonNull IntValueEnum value, IntValueEnum... values) {
        return !isIn(value, values);
    }

    static boolean isIn(@NonNull Integer value, IntValueEnum... values) {
        long containCount = Stream.of(values)
                .filter(x -> Objects.equals(value, x.getIntValue()))
                .count();
        return containCount > 0;
    }


    static <T extends IntValueEnum> T valueOf(int intValue, Class<T> clazz) {
        return valueOf(intValue, clazz, false);
    }

    static <T extends IntValueEnum> T valueOf(int val, Class<T> clazz, boolean silent) {
        return valueOf(
                val,
                (v, intVal) -> Objects.equals(v, intVal.intValue()),
                clazz,
                silent
        );
    }


    static <T extends IntValueEnum, V> T valueOf(V val, BiPredicate<V, IntValueEnum> biPredicate, Class<T> clazz, boolean silent) {
        IntValueEnum[] enums = clazz.getEnumConstants();
        if (enums == null) {
            if (silent) {
                return null;
            } else {
                throw new UnsupportedOperationException("no enum constants get from class " + clazz);
            }
        } else {
            Optional<IntValueEnum> opt = Stream.of(enums)
                    .filter(x -> biPredicate.test(val, x))
                    .findFirst();
            if (opt.isPresent()) {
                return (T) opt.get();
            }
            if (silent) {
                return null;
            } else {
                throw new IllegalArgumentException("no enum found for value " + val + " in class " + clazz);
            }
        }
    }


    static <T extends IntValueEnum> T valueOf(String name, Class<T> clazz) {
        return valueOf(name, clazz, false);
    }

    static <T extends IntValueEnum> T valueOf(String name, Class<T> clazz, boolean silent) {
        return valueOf(
                name,
                (n, intVal) -> ((Enum<?>) intVal).name().equals(n),
                clazz,
                silent
        );
    }

    static List<Integer> toIntList(List<? extends IntValueEnum> intValueEnums) {
        return Optional.ofNullable(intValueEnums)
                .orElse(Collections.emptyList())
                .stream()
                .map(IntValueEnum::intValue)
                .collect(Collectors.toList());
    }

    static List<Long> toLongList(List<? extends IntValueEnum> intValueEnums) {
        return toIntList(intValueEnums)
                .stream()
                .map(Integer::longValue)
                .collect(Collectors.toList());
    }

    static <T extends IntValueEnum> List<T> fromLongList(List<Long> values, Class<T> clazz) {
        return Optional.ofNullable(values)
                .orElse(Collections.emptyList())
                .stream()
                .map(x -> valueOf(x.intValue(), clazz))
                .collect(Collectors.toList());
    }

    static <T extends IntValueEnum> List<T> fromIntList(List<Integer> values, Class<T> clazz) {
        return Optional.ofNullable(values)
                .orElse(Collections.emptyList())
                .stream()
                .map(x -> valueOf(x, clazz))
                .collect(Collectors.toList());
    }

    static <T extends IntValueEnum> List<T> fromStringList(List<String> names, Class<T> clazz) {
        return Optional.ofNullable(names)
                .orElse(Collections.emptyList())
                .stream()
                .map(x -> valueOf(x, clazz))
                .collect(Collectors.toList());
    }
}
