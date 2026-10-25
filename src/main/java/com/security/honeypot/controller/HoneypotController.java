package com.security.honeypot.controller;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import com.security.honeypot.model.AttackLog;
import com.security.honeypot.repository.AttackLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/bank")
public class HoneypotController {

    private final AttackLogRepository attackLogRepository;

    public HoneypotController(AttackLogRepository attackLogRepository) {
        this.attackLogRepository = attackLogRepository;
    }

    // --- 📲 NAYA FEATURE: TELEGRAM ALERTS ---
    private void sendTelegramAlert(String ipAddress, String attackType, String location) {
        String botToken = "YOUR_TELEGRAM_TOKEN";
        String chatId = "YOUR_CHAT_ID";

        String text = "🚨 *SHADOWVAULT CRITICAL ALERT* 🚨\n\n" +
                "⚠️ Attack Detected!\n" +
                "🌐 IP Address: " + ipAddress + "\n" +
                "📍 Location: " + location + "\n" +
                "🎯 Target: Bitcoin Wallet (wallet.dat)\n" +
                "🛑 Type: " + attackType;

        String url = "https://api.telegram.org/bot" + botToken + "/sendMessage?chat_id=" + chatId + "&text=" + text + "&parse_mode=Markdown";

        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getForObject(url, String.class);
            System.out.println("Telegram Alert Sent Successfully!");
        } catch (Exception e) {
            System.out.println("Telegram Alert Error: " + e.getMessage());
        }
    }

    // --- 🌍 NAYA FEATURE: IP GEOLOCATION TRACKER ---
    private String trackLocation(String ip) {
        if (ip.equals("127.0.0.1") || ip.equals("0:0:0:0:0:0:0:1")) {
            return "Localhost (Test Network)";
        }
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://ip-api.com/json/" + ip;
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response != null && "success".equals(response.get("status"))) {
                return response.get("city") + ", " + response.get("country");
            }
        } catch (Exception e) {
            System.out.println("Location tracking failed: " + e.getMessage());
        }
        return "Unknown Location";
    }

    // --- 💬 NAYA FEATURE: TWILIO SMS ALERTS ---
    private void sendSmsAlert(String ipAddress, String attackType) {
        String ACCOUNT_SID = "YOUR_TWILIO_SID";
        String AUTH_TOKEN = "YOUR_TWILIO_TOKEN";

        String twilioNumber = "+17372508034";
        String myNumber = "+919219710250";

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        // YAHAN EXACT TWILIO TEMPLATE TEXT SET KIYA HAI
        String smsText = "sms_appointment_reminders";

        try {
            Message message = Message.creator(
                    new PhoneNumber(myNumber),
                    new PhoneNumber(twilioNumber),
                    smsText
            ).create();
            System.out.println("SMS Alert Sent! SID: " + message.getSid());
        } catch (Exception e) {
            System.out.println("SMS Alert Error: " + e.getMessage());
        }
    }

    @GetMapping("/customers/export")
    public List<Map<String, String>> exportFakeCustomers(HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        long previousAttacks = attackLogRepository.countByIpAddress(ipAddress);

        int threatScore = 10;
        String attackType = "Human / Manual Browser";

        if (userAgent != null) {
            String agentLower = userAgent.toLowerCase();
            if (agentLower.contains("python") || agentLower.contains("curl") ||
                    agentLower.contains("postman") || agentLower.contains("nmap") || agentLower.contains("java")) {
                attackType = "Automated Hacker Bot 🤖";
                threatScore += 50;
            }
        }

        if (previousAttacks > 1) {
            threatScore += (previousAttacks * 15);
        }

        if (threatScore > 100) threatScore = 100;

        AttackLog log = new AttackLog();
        log.setIpAddress(ipAddress);
        log.setUserAgent(userAgent);
        log.setEndpointAccessed("/api/v1/bank/customers/export");
        log.setAttackTime(LocalDateTime.now());
        log.setThreatScore(threatScore);
        log.setAttackType(attackType);
        log.setLocation(trackLocation(ipAddress));

        attackLogRepository.save(log);

        long delaySeconds = (previousAttacks + 1) * 3;
        if (delaySeconds > 15) delaySeconds = 15;

        try {
            Thread.sleep(delaySeconds * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        List<Map<String, String>> fakeDataList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Map<String, String> fakeCustomer = new HashMap<>();
            fakeCustomer.put("customerId", "CUST-" + (8000 + i));
            fakeCustomer.put("name", "Test_User_" + i);
            fakeDataList.add(fakeCustomer);
        }

        return fakeDataList;
    }

    @GetMapping("/admin/logs")
    public List<AttackLog> getAllAttackLogs() {
        return attackLogRepository.findAll();
    }

    @PostMapping("/admin/login")
    public String trapAdminLogin(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpServletRequest request) throws InterruptedException {

        String ipAddress = request.getRemoteAddr();
        Thread.sleep(4000);

        AttackLog log = new AttackLog();
        log.setIpAddress(ipAddress);
        log.setEndpointAccessed("/admin/login");
        log.setAttackTime(LocalDateTime.now());
        log.setThreatScore(99);
        log.setAttackType("Honeytoken (Admin Brute-Force)");
        log.setUserAgent("TRAPPED CREDENTIALS 🚨 -> User: " + username + " | Pass: " + password);
        log.setLocation(trackLocation(ipAddress));

        attackLogRepository.save(log);
        return "Authentication Failed. Invalid Credentials.";
    }

    @GetMapping("/admin/backup/wallet.dat")
    public String trapCryptoThief(HttpServletRequest request) throws InterruptedException {
        String ipAddress = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        Thread.sleep(5000);

        AttackLog log = new AttackLog();
        log.setIpAddress(ipAddress);
        log.setUserAgent(userAgent != null ? userAgent : "Unknown Hacker Script");
        log.setEndpointAccessed("/admin/backup/wallet.dat");
        log.setAttackTime(LocalDateTime.now());
        log.setThreatScore(100);
        log.setAttackType("CRITICAL: Crypto Theft Attempt 🚨");

        String loc = trackLocation(ipAddress);
        log.setLocation(loc);

        attackLogRepository.save(log);

        // 📲 TELEGRAM ALERT
        sendTelegramAlert(ipAddress, log.getAttackType(), loc);

        // 💬 TWILIO SMS ALERT
        sendSmsAlert(ipAddress, log.getAttackType());
hone
        return "ERR_CORRUPT_FILE_0x88A1: Encrypted RSA Keys cannot be read. Master passphrase required.";
    }
}