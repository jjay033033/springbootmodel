package top.lmoon.util;

public class ValidateUtil {
	
	private static final String REGEX_NUMBER_LETTER = "[A-Za-z0-9]+";
	
	private static final String REGEX_CHINESE = "[\u4e00-\u9fa5]+";
	
	private static final String REGEX_CHINESE_NUMBER_LETTER = "[\u4e00-\u9fa5_A-Za-z0-9]+";
	
    public static boolean isValidURL(String input) {
        if (input == null || input.length() < 8) {
            return false;
        }
        char ch0 = input.charAt(0);
        if (ch0 == 'h') {
            if (input.charAt(1) == 't' &&
                input.charAt(2) == 't' &&
                input.charAt(3) == 'p') {
                char ch4 = input.charAt(4);
                if (ch4 == ':') {
                    if (input.charAt(5) == '/' &&
                        input.charAt(6) == '/') {

                        return isValidURLChar(input, 7);
                    } else {
                        return false;
                    }
                } else if (ch4 == 's') {
                    if (input.charAt(5) == ':' &&
                        input.charAt(6) == '/' &&
                        input.charAt(7) == '/') {

                        return isValidURLChar(input, 8);
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }

        } else if (ch0 == 'f') {
            if( input.charAt(1) == 't' &&
                input.charAt(2) == 'p' &&
                input.charAt(3) == ':' &&
                input.charAt(4) == '/' &&
                input.charAt(5) == '/') {

                return isValidURLChar(input, 6);
            } else {
                return false;
            }
        }
        return false;
    }

    static boolean isValidURLChar(String url, int start) {
        for (int i = start, c = url.length(); i < c; i ++) {
            char ch = url.charAt(i);
            if (ch == '"' || ch == '\'') {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isNumberOrLetter(String src){
    	return src.matches(REGEX_NUMBER_LETTER);
    }
    
    public static boolean isChinese(String src){
    	return src.matches(REGEX_CHINESE);
    }
    
    public static boolean isChineseOrNumberOrLetter(String src){
    	return src.matches(REGEX_CHINESE_NUMBER_LETTER);
    }

    public static void main(String[] args) {
//		System.out.println(encode("<"));
//		System.out.println(encodeForCSS("<"));
//		System.out.println(encodeForJS("<"));
//		System.out.println(encodeURIComponent("<"));
		System.out.println(isChineseOrNumberOrLetter("我师父dfd13234E_"));
	}

}
