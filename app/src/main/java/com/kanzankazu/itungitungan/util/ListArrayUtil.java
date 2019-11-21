package com.kanzankazu.itungitungan.util;

import android.annotation.SuppressLint;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ListArrayUtil {

    //int
    public static Integer[] convertListIntegertToIntegerArray(List<Integer> result) {
        Integer[] finalResult = new Integer[result.size()];
        return result.toArray(finalResult);
    }

    public static int[] convertListIntegertToIntArray(List<Integer> list) {
        int[] ret = new int[list.size()];
        int i = 0;
        for (Integer e : list)
            ret[i++] = e.intValue();
        return ret;
    }

    @SuppressLint("NewApi")
    public static List<Integer> convertIntArrayToListIntegert(int[] data) {
        return Arrays.stream(data).boxed().collect(Collectors.toList());
    }

    @SuppressLint("NewApi")
    public static Integer[] convertIntArrayToIntegertArray(int[] data) {
        return Arrays.stream(data).boxed().toArray(Integer[]::new);
    }

    @SuppressLint("NewApi")
    public static int[] convertIntegertArrayToIntArray(Integer[] array) {
        return Arrays.stream(array).mapToInt(Integer::intValue).toArray();
    }

    //String
    public static ArrayList<String> convertStringArrayToListString(String[] strings) {
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < strings.length; i++) {
            arrayList.add(strings[i]);
        }
        return arrayList;
    }

    public static ArrayList<String> convertStringArrayToListString1(String... strings) {
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < strings.length; i++) {
            arrayList.add(strings[i]);
        }
        return arrayList;
    }

    public static String[] convertStringToStringArray(String s) {
        return s.split(",");
    }

    public static List<String> convertStringToListString(String s) {
        String[] strings = s.split(",");
        return Arrays.asList(strings);
    }

    public static String convertListStringToString(ArrayList<String> stringList, String demiliter) {
        String[] myStringList = stringList.toArray(new String[stringList.size()]);
        return TextUtils.join(demiliter, myStringList);
    }

    public static String convertListStringToString1(ArrayList<String> stringList) {
        String[] myStringList = stringList.toArray(new String[stringList.size()]);
        return TextUtils.join("‚‗‚", myStringList);

    }

    public static String convertListStringToStringComma(ArrayList<String> stringList) {
        return TextUtils.join(",", stringList);
    }

    public static ArrayList<String> convertStringToListString1(String s) {
        return new ArrayList<String>(Arrays.asList(TextUtils.split(s, "‚‗‚")));
    }

    public static String[] convertStringToArrayString1(String s) {
        return s.split("‚‗‚");
    }

    public static String strSeparator = "__,__";

    public static String convertStringArrayToString2(String[] array) {
        String str = "";
        for (int i = 0; i < array.length; i++) {
            str = str + array[i];
            // Do not append comma at the end of last element
            if (i < array.length - 1) {
                str = str + strSeparator;
            }
        }
        return str;
    }

    public static String[] convertStringToStringArray2(String str) {
        String[] arr = str.split(strSeparator);
        return arr;
    }

    public static boolean isListContainInt(final int[] array, final int key) {
        Arrays.sort(array);
        return Arrays.binarySearch(array, key) >= 0;
    }

    public static boolean isListContainString(List<String> sourceList, String s) {
        return sourceList.contains(s);
    }

    public static boolean isListContainStringArray(List<String> sourceList, String stringArray) {
        String[] strings = stringArray.split(",");
        List<String> stringList = Arrays.asList(strings);
        return sourceList.containsAll(stringList);
    }

    public static boolean isListContainStringList(List<String> sourceList, List<String> stringList) {
        return sourceList.containsAll(stringList);
    }

    public static List<Integer> getAllSizeStringList(List<String> sourceList) {
        List<Integer> integers = new ArrayList<>();
        for (int i = 0; i < sourceList.size(); i++) {
            integers.add(i);
        }
        return integers;
    }

    public static int getPosStringInList(List<String> sourceList, String s) {
        int position = -1;
        for (int i = 0; i < sourceList.size(); i++) {
            if (sourceList.get(i).equalsIgnoreCase(s)) {
                position = i;
            }
        }
        return position;
    }

    public static List<Integer> getPosListStringInList(List<String> sourceList, String s) {
        List<Integer> integers = new ArrayList<>();
        for (int i = 0; i < sourceList.size(); i++) {
            if (sourceList.get(i).equalsIgnoreCase(s)) {
                integers.add(i);
            }
        }
        return integers;
    }

    public static List<Integer> getInvPosListStringInList(List<String> sourceList, String s) {
        List<Integer> integers = new ArrayList<>();
        for (int i = 0; i < sourceList.size(); i++) {
            if (!sourceList.get(i).equalsIgnoreCase(s)) {
                integers.add(i);
            }
        }
        return integers;
    }

    public static List<Integer> getPosListStringIn2List(List<String> sourceList, List<String> s) {
        List<Integer> integers = new ArrayList<>();
        for (int i = 0; i < sourceList.size(); i++) {
            for (String ss : s) {
                if (sourceList.get(i).equalsIgnoreCase(ss)) {
                    integers.add(i);
                }
            }

        }
        return integers;
    }

    public static List<Integer> getInvPosListStringIn2List(List<String> sourceList, List<String> s) {
        List<Integer> integers = new ArrayList<>();
        for (int i = 0; i < sourceList.size(); i++) {
            for (String ss : s) {
                if (!sourceList.get(i).equalsIgnoreCase(ss)) {
                    integers.add(i);
                }
            }

        }
        return integers;
    }

    public static String[] convertListStringToArrayString(List<String> sourceList) {
        return sourceList.toArray(new String[0]);
    }
}
