# ShadowVault - Spring Boot Honeypot 🛡️

A cybersecurity honeypot project built using Java and Spring Boot. The main goal of this project is to create fake endpoints (like a dummy admin login or fake crypto wallet) to attract malicious attackers, log their details, and send real-time alerts.

I developed this project to understand how attackers target web applications and how we can track their behavior using logging, threat scoring, and API integrations.

### Key Features
* **Decoy Endpoints:** Fake URLs designed to look like sensitive data (e.g., `/admin/login`, `/customers/export`).
* **Attacker Logging:** Captures the IP address, User-Agent, and timestamp of any unauthorized access attempt.
* **IP Geolocation:** Uses an external API to track the physical location (City/Country) of the attacker.
* **Tarpitting (Delay Mechanism):** Intentionally slows down the server response for repeat offenders to waste automated bot resources.
* **Real-time Alerts:** Sends instant notifications via Telegram Bot and Twilio SMS if a high-risk file is accessed.

### Technology Stack
* **Language:** Java
* **Framework:** Spring Boot, Spring Web
* **Database:** H2 Database (In-memory, makes testing easy without complex setups)
* **Integrations:** Twilio (SMS), Telegram Bot API, IP-API (Geolocation)

### How to Run This Project
1. Clone this repository to your local machine.
2. Open the project in IntelliJ IDEA.
3. Add your Twilio and Telegram API keys in the `HoneypotController.java` file.
4. Run the `HoneypotApplication.java` main class.
5. The application will start on port `8083`.

### Endpoints to Test
You can use Postman or your web browser to test these traps:
* `GET /api/v1/bank/customers/export` - Simulates a fake customer data export (adds a delay based on previous visits).
* `POST /api/v1/bank/admin/login` - Fake admin panel login trap.
* `GET /api/v1/bank/admin/backup/wallet.dat` - High-risk trap that triggers SMS and Telegram alerts.

---
*Developed as an academic mini-project for educational and research purposes.*
