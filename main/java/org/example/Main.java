package org.example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.Authenticator;


    //NOT*** Projemi oluştururken maven kullandım düzgün çalışması için pom.xml file içine javamail dependency indirilmeli**
    public class Main {
        static class Member {
//Main sınıfının alt sınıfı olacak şekilde iç içe sınıf oluşturdum

            private String name;
            private String surname;
            private String email;

            public Member() {
            }

            public Member(String name, String surname, String email) {
                this.name = name;
                this.surname = surname;
                this.email = email;
            }

            public String getName() {
                return name;
            }

            public String getSurname() {
                return surname;
            }

            public String getEmail() {
                return email;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setSurname(String surname) {
                this.surname = surname;
            }

            public void setEmail(String email) {
                this.email = email;
            }


        }

        public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);
            ArrayList<Member> eliteMembers = new ArrayList<>();
            ArrayList<Member> regularMembers = new ArrayList<>();
//her grup için depo alanı olarak Arraylist oluşturdum
            while (true) {
                System.out.println("Select an option:");
                System.out.println("1- Add elite member");
                System.out.println("2- Add regular member");
                System.out.println("3- Send mail");
                int choice = scanner.nextInt();
                scanner.nextLine();
//Kullanıcı girdi değişkeni için her seçeneği switch kullanarak yol alıyorum
                switch (choice) {
                    case 1:
                        System.out.print("Enter name: ");
                        String eliteName = scanner.nextLine();
                        System.out.print("Enter surname: ");
                        String eliteSurname = scanner.nextLine();
                        System.out.print("Enter email: ");
                        String eliteEmail = scanner.nextLine();
                        eliteMembers.add(new Member(eliteName, eliteSurname, eliteEmail));
                        writeToFile("elite_members.txt", eliteName + "\t" + eliteSurname + "\t" + eliteEmail);
                        break;

                    case 2:
                        System.out.print("Enter name: ");
                        String regularName = scanner.nextLine();
                        System.out.print("Enter surname: ");
                        String regularSurname = scanner.nextLine();
                        System.out.print("Enter email: ");
                        String regularEmail = scanner.nextLine();
                        regularMembers.add(new Member(regularName, regularSurname, regularEmail));
                        writeToFile("regular_members.txt", regularName + "\t" + regularSurname + "\t" + regularEmail);
                        break;

                    case 3:
                        System.out.println("Select an option:");
                        System.out.println("1- Send mail to elite members");
                        System.out.println("2- Send mail to regular members");
                        System.out.println("3- Send mail to all members");
                        int mailChoice = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Enter mail subject: ");
                        String mailSubject = scanner.nextLine();
                        System.out.print("Enter mail message: ");
                        String mailMessage = scanner.nextLine();
//Mail gönderme seçeneği için hangi gruba mail atılacağı switch ile belirledim
                        switch (mailChoice) {
                            case 1:
                                sendMail(eliteMembers, mailSubject, mailMessage);
                                break;

                            case 2:
                                sendMail(regularMembers, mailSubject, mailMessage);
                                break;

                            case 3:
                                ArrayList<Member> allMembers = new ArrayList<>(eliteMembers);
                                allMembers.addAll(regularMembers);
                                sendMail(allMembers, mailSubject, mailMessage);
                                break;

                            default://eğer farklı bir değer girilirse geçersiz sayılacak
                                System.out.println("Invalid choice!");
                                break;
                        }
                        break;

                    default:
                        System.out.println("Invalid choice!");
                        break;
                }
            }
        }

        //girilen kullanıcıları dosyaya yazdırma işlemi yapıyorum
        public static void writeToFile(String fileName, String data) {
            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(new FileWriter(fileName));
                writer.write(data);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (writer != null) {
                        writer.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //mail gönderme methodunu izinleri ile birlikte yazıyorum
        public static void sendMail(ArrayList<Member> members, String subject, String message) {
            // Set up mail properties
            Properties properties = new Properties();
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.port", "587");

            // Set up mail session
            Session session = Session.getInstance(properties, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("esraakyol787@gmail.com", "12BNM345");
                }
            });

            try {
                // Create a message
                Message mailMessage = new MimeMessage(session);
                mailMessage.setFrom(new InternetAddress("esraakyol787@gmail.com"));
                for (Member member : members) {
                    mailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(member.getEmail()));
                }
                mailMessage.setSubject(subject);
                mailMessage.setText(message);

                // Send the message
                Transport.send(mailMessage);

                System.out.println("Mail sent successfully!");

            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }



    }
