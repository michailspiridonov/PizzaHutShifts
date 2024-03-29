import javax.mail.Message;
import javax.mail.MessagingException;
import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;


public class PizzaHutShifts {
    public static void main(String[] args) throws IOException, MessagingException {
        isSaved();
        ArrayList<Message> shiftMails = MailExtractor.getMails();
        ContentFormatter.getDate(shiftMails.get(shiftMails.size()-1).getSubject());
        ArrayList<String[]> shifts = ContentFormatter.extractShiftTimes(shiftMails.get(shiftMails.size()-1));
        PrintWriter pw = new PrintWriter(new FileWriter(new File("shifts")));
        for(String[] shift : shifts){
            pw.println(ContentFormatter.printShifts(shift)+"\n");
            System.out.println(ContentFormatter.printShifts(shift));
        }
        pw.close();
    }

    private static void isSaved(){
        File file = new File("date");
        try {
            Scanner sc = new Scanner(file);
            Scanner ssc = new Scanner(System.in);
            DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
            Date curDate = new Date();
            Date savedDate = dateFormat.parse(sc.next());
            long diff = curDate.getTime() - savedDate.getTime();
            long diffDays = diff / (24 * 60 * 60 * 1000);
            if(diffDays>3){
                System.out.println("saved date is too old, downloading new mails");
            } else {
                System.out.println("saved date is less than 4 days old, download new mails? Y/N");
                String s = ssc.nextLine();
                if(s.equals("Y")){
                    System.out.println("downloading mails");
                    return;
                } else {
                    System.out.println("showing saved shifts");
                    sc = new Scanner(new File("shifts"));
                    while(sc.hasNext()){
                        System.out.println(sc.nextLine());
                    }
                    System.exit(0);
                }
            }
            System.out.println(diffDays);
        } catch (FileNotFoundException e) {
            System.out.println("no saved date found, downloading new mails");
        } catch (ParseException e) {
            System.out.println("wrong date saved, downloadin mails");
        }
    }
}
