import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws InterruptedException{
        while (true) {
            try{
                Parser.Item item = Parser.getCurrencyExchange("USD");
                //DB.myCreate();
                DB.myInsert(item.getDate(), item.getValue());
                DB.mySelect();
                TimeUnit.HOURS.sleep(12);
            } catch (Exception e) {
                e.printStackTrace();
                TimeUnit.MINUTES.sleep(5);
            }
        }

    }





}

