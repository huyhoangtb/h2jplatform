/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.core.utils;

/**
 * huyhoang for java platform. (h2j-platform)
 * @author HuyHoang
 * @Email: huyhoang85_tb@yahoo.com
 * @Tel: 0966298666
 */
public class ConvertCurencyFromNumberToText {

    /**
     * Đọc tối đa 100.000.000.000.000.000 999.999.999.999.999.999
     *
     * @param number
     * @return
     */
    public static String converterNumber(Double number) {
        if (number > 9999999999999999D) {
            return number.toString();
        }
        StringBuilder result = new StringBuilder();
        Double phanDu = 0D;
        Integer tyTy = Double.valueOf(number / 1000000000000000D).intValue();
        if (tyTy > 0) {
            result.append(readingNumber(tyTy)).append("tỷ tỷ ");
        }
        phanDu = number % 1000000000000000D;
        Integer nghinTy = Double.valueOf(phanDu / 1000000000000D).intValue();

        if (nghinTy > 0) {
            result.append(readingNumber(nghinTy)).append("nghìn  ");
        }

        phanDu = number % 1000000000000D;
        Integer ty = Double.valueOf(phanDu / 1000000000D).intValue();

        if (ty > 0) {
            result.append(readingNumber(ty)).append("tỷ ");
        }

        phanDu = number % 100000000000D;
        Integer tramtrieu = Double.valueOf(phanDu / 100000000D).intValue();

        if (tramtrieu > 0) {
            result.append(readingNumber(tramtrieu)).append("trăm ");
        }
        phanDu = number % 100000000D;
        Integer trieu = Double.valueOf(phanDu / 1000000D).intValue();

        if (trieu > 0) {
            result.append(readingNumber(trieu)).append("triệu ");
        }

        phanDu = number % 1000000D;
        Integer nghin = Double.valueOf(phanDu / 1000D).intValue();

        if (nghin > 0) {
            result.append(readingNumber(nghin)).append("nghìn ");
        }
        phanDu = number % 1000D;
        Integer tram = Double.valueOf(phanDu / 100D).intValue();

        if (tram > 0) {
            result.append(readingNumber(tram)).append("trăm ");
        }
        phanDu = number % 100D;
        Integer chuc = Double.valueOf(phanDu / 10D).intValue();

        if (chuc > 0) {
            result.append(readingNumber(chuc)).append("mươi ");
        }
        phanDu = number % 10D;
        if (phanDu.intValue() > 0) {
            result.append(readingNumber(phanDu.intValue())).append("đồng ");
        } else {
            result.append("đồng ");
        }



        return result.toString();
    }

    public static void main(String[] agrs) {

        System.out.println("readingThousand: " + converterNumber(1D));
    }

    public static StringBuilder readingNumber(Integer number) {

        String nString = number.toString();
        switch (nString.length()) {
            case 3:
                return readingPercent(nString);
            case 2:
                return readingDozens(nString);
            case 1:
                return new StringBuilder(converNumberUnitToText(number));
        }
        return new StringBuilder();
    }

//    public static String readingThousand(Integer number) {//(100).000
//        return readingPercent(number).append("nghìn").toString();
//    }
    /**
     * Đọc hàng trăm
     *
     * @return
     */
    public static StringBuilder readingPercent(String numberString) {
        StringBuilder stb = new StringBuilder();
        if (numberString.length() == 2) {
            numberString = "0" + numberString;
        }
        stb.append(converPercentToText(Integer.valueOf(String.valueOf(numberString.charAt(0)))));
        stb.append(converDozensToText(Integer.valueOf(String.valueOf(numberString.charAt(1))), Integer.valueOf(String.valueOf(numberString.charAt(2)))))
                .append(converNumberToText(Integer.valueOf(String.valueOf(numberString.charAt(2)))));
        return stb;
    }

    public static StringBuilder readingDozens(String numberString) {
        StringBuilder stb = new StringBuilder();
        stb.append(converDozensToText(Integer.valueOf(String.valueOf(numberString.charAt(0))), Integer.valueOf(String.valueOf(numberString.charAt(1)))));
        stb.append(converNumberToText(Integer.valueOf(String.valueOf(numberString.charAt(1)))));
        return stb;

    }

    /**
     * Đọc hàng đơn vị
     *
     * @return
     */
//    public String readingUnit() {
//    }
    public static String converUnit(Integer number) {
        String numberString = number.toString();
        if (numberString.length() == 7) {//1000000
            return "tỷ ";
        }
        if (numberString.length() == 4) {
            return "nghìn ";
        }
        if (numberString.length() == 3) {
            return "trăm ";
        }
        if (numberString.length() == 2) {
            return "mươi ";
        }
        return "????";

    }

    public static String converPercentToText(Integer number) {
        switch (number) {
            case 0:
                return "không trăm ";
            case 1:
                return "một trăm ";
            case 2:
                return "hai trăm ";
            case 3:
                return "ba trăm ";
            case 4:
                return "bốn trăm ";
            case 5:
                return "năm trăm ";
            case 6:
                return "sáu trăm ";
            case 7:
                return "bảy trăm ";
            case 8:
                return "tám trăm ";
            default:
                return "chín trăm ";
        }
    }

    public static String converDozensToText(Integer number, Integer lastNumber) {
        switch (number) {
            case 0:
                if (lastNumber.intValue() == 0) {
                    return "";
                }
                return "linh ";
            case 1:
                return "mười ";
            case 2:
                return "hai mươi ";
            case 3:
                return "ba mươi ";
            case 4:
                return "bốn mươi ";
            case 5:
                return "năm mươi ";
            case 6:
                return "sáu mươi ";
            case 7:
                return "bảy mươi ";
            case 8:
                return "tám mươi ";
            default:
                return "chín mươi ";
        }
    }

    public static String converNumberUnitToText(Integer number) {
        switch (number) {
            case 0:
                return ""; // khong thi khong đọc 10 đọc là mười chứ khong đọcl à mười không
            case 1:
                return "một ";
            case 2:
                return "hai ";
            case 3:
                return "ba ";
            case 4:
                return "bốn ";
            case 5:
                return "năm ";
            case 6:
                return "sáu ";
            case 7:
                return "bảy ";
            case 8:
                return "tám ";
            default:
                return "chín ";
        }
    }
    public static String converNumberToText(Integer number) {
        switch (number) {
            case 0:
                return ""; // khong thi khong đọc 10 đọc là mười chứ khong đọcl à mười không
            case 1:
                return "một ";
            case 2:
                return "hai ";
            case 3:
                return "ba ";
            case 4:
                return "bốn ";
            case 5:
                return "lăm ";
            case 6:
                return "sáu ";
            case 7:
                return "bảy ";
            case 8:
                return "tám ";
            default:
                return "chín ";
        }
    }
}
