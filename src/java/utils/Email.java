/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.internet.MimeUtility;

/**
 *
 * @author ACER
 */
public class Email {
    //vào bảo mật -> vô mật khẩu ứng dụng -> tạo 1 cái tựa để họ cho mật khẩu
    //Email: nguyenminhquan52055205@gmail.com
    //Password: hnyralbcmjetgspt
    static final String from = "yourbookly@gmail.com";
    static final String password = "xtzytaiphcwstgre";
    
    //hàm giúp mình đăng nhập vào hệ thống
    public static boolean sendEmail(String to ,String tieuDe, String content){
        //properties: khai báo các thuộc tính 
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");//SMTP HOST
        props.put("mail.smtp.port", "smtp.gmail.com");//TLS 587  SSL 465
        //thông thường có 2 cái port : 1 là port 465 hai là 587()
        props.put("mail.smtp.auth", "true");//dùng host này để gửi mail thì phải đăng nhập 
        props.put("mail.smtp.starttls.enable", "true");

        // create Authenticator(thông qua hàm này chày ta sẽ lấy ra 1 đối tượng authenticator để đăng nhập vào gmail)
        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        };

        //Phiên làm việc (quản lý học thư của mình )
        //viết ra 1 phần mềm để đọc email 
        Session sesstion = Session.getInstance(props, auth);//thông qua cái này chúng ta sẽ đăng nhập vào thằng gmail(props) với tài khoản này(auth)

        //tạo 1 tin nhắn 
        MimeMessage msg = new MimeMessage(sesstion);
        try {
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");//email của mình có content-type là kiểu gì: có thể là text hay text-html
            //người gửi 
            msg.setFrom(from);//email này được gửi từ đâu

            //người nhận
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));//set với người nhận

            //Tiêu đề email 
            msg.setSubject(MimeUtility.encodeText(tieuDe, "UTF-8", "B"));

            //Quy định ngày gửi 
            msg.setSentDate(new Date());

            //Quy định email nhận phản hồi 
            //msg.setReplyTo(InternetAddress.parse(to, false));nhận lại phản hòi từ khách hàng sẽ ở một email khác
            //Nội dung
            msg.setContent(content, "text/html; charset=UTF8");//mình phải setContent thì code html mới được còn nếu setText thì chỉ có văn bản mới được nhập vào 

            //gửi email
            Transport.send(msg);
            System.out.println("Send Email successfully!!!");
            return true;
        } catch (Exception e) {
            System.out.println("Error When Send Email");
            e.printStackTrace();
            return false;
        }
    }
    public static void main(String[] args) {
        Email.sendEmail("minhquan520552055205@gmail.com", System.currentTimeMillis() + "" ,"Đây la phần nội dung" );
    }
//    //
//    public static void main(String[] args) {
//        final String from = "nguyenminhquan52055205@gmail.com";
//        final String password = "hnyralbcmjetgspt";
//
//        //properties: khai báo các thuộc tính 
//        Properties props = new Properties();
//        props.put("mail.smtp.host", "smtp.gmail.com");//SMTP HOST
//        props.put("mail.smtp.port", "smtp.gmail.com");//TLS 587  SSL 465
//        //thông thường có 2 cái port : 1 là port 465 hai là 587()
//        props.put("mail.smtp.auth", "true");//dùng host này để gửi mail thì phải đăng nhập 
//        props.put("mail.smtp.starttls.enable", "true");
//
//        // create Authenticator(thông qua hàm này chày ta sẽ lấy ra 1 đối tượng authenticator để đăng nhập vào gmail)
//        Authenticator auth = new Authenticator() {
//            @Override
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication(from, password);
//            }
//        };
//
//        //Phiên làm việc (quản lý học thư của mình )
//        //viết ra 1 phần mềm để đọc email 
//        Session sesstion = Session.getInstance(props, auth);//thông qua cái này chúng ta sẽ đăng nhập vào thằng gmail(props) với tài khoản này(auth)
//
//        //Gửi email 
//        final String to = "minhquan520552055205@gmail.com";//có nhiều loại gưi như gửi cho nhiêu người gửi ẩn danh: cc, to, dcc 
//
//        //tạo 1 tin nhắn 
//        MimeMessage msg = new MimeMessage(sesstion);
//        try {
//            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");//email của mình có content-type là kiểu gì: có thể là text hay text-html
//            //người gửi 
//            msg.setFrom(from);//email này được gửi từ đâu
//
//            //người nhận
//            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));//set với người nhận
//
//            //Tiêu đề email 
//            msg.setSubject("Thử nghiệm gửi email" + System.currentTimeMillis());
//
//            //Quy định ngày gửi 
//            msg.setSentDate(new Date());
//
//            //Quy định email nhận phản hồi 
//            //msg.setReplyTo(InternetAddress.parse(to, false));nhận lại phản hòi từ khách hàng sẽ ở một email khác
//            //Nội dung
//            msg.setContent("<!DOCTYPE html>\n"
//                    + "<html>\n"
//                    + "<head>\n"
//                    + "<title>Page Title</title>\n"
//                    + "</head>\n"
//                    + "<body>\n"
//                    + "\n"
//                    + "<h1>My First Heading</h1>\n"
//                    + "<p>My first paragraph.</p>\n"
//                    + "\n"
//                    + "</body>\n"
//                    + "</html>","text/html");//mình phải setContent thì code html mới được còn nếu setText thì chỉ có văn bản mới được nhập vào 
//
//            //gửi email
//            Transport.send(msg);
//            System.out.println("Gửi email thành công");
//        } catch (Exception e) {
//            System.out.println("Gặp lỗi trong quá trình gửi email");
//            e.printStackTrace();
//        }
//    }
}
