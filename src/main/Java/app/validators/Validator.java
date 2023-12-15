package app.validators;

public class Validator {


    public static String userInput(String formParam, String alt){
        if(validateString(formParam)){
            return formParam;
        }
        return alt;
    }

    public static int userInput(String formParam, int alt){
        if(validateInt(formParam)){
            return Integer.parseInt(formParam);
        }
        return alt;
    }

    public static double userInput(String formParam, double alt){
        if(validateDouble(formParam)){
            return Double.parseDouble(formParam);
        }
        return alt;
    }

    private static boolean validateDouble(String str){
        if(str == null || str.isEmpty()){
            return false;
        }

        if(!containsOnlyNumbersAndDot(str)){
            return false;
        }

        return true;
    }

    public static boolean containsOnlyNumbersAndDot(String str){
        return str.matches("[0-9]+(\\.[0-9]+)?");
    }

    private static boolean validateInt(String str){
        if(str == null || str.isEmpty()){
            return false;
        }

        if(containsOnlyNumbers(str)){
            int number = Integer.parseInt(str);
            if(number <= 0){
                return false;
            }
            return true;
        }

        return false;
    }

    private static boolean validateString(String str){
        if(str == null || str.isEmpty()){
            return false;
        }
        return true;
    }

    private static boolean containsOnlyNumbers(String str){
        for(char c : str.toCharArray()){
            if(!Character.isDigit(c)){
                return false;
            }
        }
        return true;
    }
}
