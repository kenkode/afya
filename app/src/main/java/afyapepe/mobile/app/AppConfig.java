package afyapepe.mobile.app;

public class AppConfig {

	public static String BASE_URL = "http://10.0.2.2:81/afyapepe-v1.0/public/";

	//public static String BASE_URL = "http://afyapepe.com:8100/";

	public static String URL_ALL_PATIENTS = BASE_URL + "getallpatients";

	public static String URL_PATIENTS = BASE_URL + "getpatients";

	public static String URL_WAITING_LIST = BASE_URL + "getwaitinglist";

	public static String URL_GET_COUNT = BASE_URL + "getcount";

	// Server user login url
	//public static String URL_LOGIN = "http://afyapepe.com/afyapepe/logincop.php";

	public static String URL_LOGIN = "http://10.0.2.2:81/afyapepe-v1.0/logincop.php";

	// Server user register url
	public static String URL_REGISTER = "http://afyapepe.com/afyapepe/register.php";
}
