package swp.internmanagement.internmanagement.service;

public interface SmsService {
    void sendSms(String toPhoneNumber, String messageBody);
}
