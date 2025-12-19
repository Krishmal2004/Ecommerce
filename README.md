# 🚀 WealthWise - AI-Powered Financial Tracking Platform

> Financial tracking meets Frontier AI: WealthWise is now powered by Gemini 2.5!

WealthWise is a full-stack financial analytics application that doesn't just record transactions—it understands them.  Powered by Google's Gemini 2.5 AI, the app provides personalized spending insights and intelligent budget forecasts to help users make smarter financial decisions.

## ✨ Features

- 🤖 **AI-Powered Analytics**: Integrated Gemini 2.5 API for deep financial insights and natural language understanding of spending habits
- 📊 **Real-Time Dashboard**: Custom, responsive visualization of financial data
- 🔐 **Secure Authentication**: Social login via Google and GitHub using OAuth2
- 💳 **Payment Integration**: Stripe integration for secure payment processing
- 📧 **Automated Notifications**: Gmail/SMTP confirmations sent upon successful transactions
- 📈 **Budget Forecasting**: AI-driven predictions based on historical spending patterns
- 🎯 **Personalized Insights**: Smart recommendations tailored to individual financial behaviors

## 🏗️ Tech Stack

### Frontend
- HTML5
- CSS3
- Vanilla JavaScript
- Responsive Design

### Backend
- Java
- Spring Boot
- RESTful API Architecture

### AI & Analytics
- Google Gemini 2.5 API
- Natural Language Processing
- Predictive Analytics

### Security & Authentication
- OAuth2 (Google & GitHub)
- Spring Security

### Payment & Notifications
- Stripe Payment Gateway
- Gmail/SMTP Integration

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- Maven
- Node.js (for frontend build tools, if applicable)
- Google Gemini API Key
- Stripe API Keys
- OAuth2 credentials (Google & GitHub)

### Installation

1. **Clone the repository**
```bash
git clone https://github.com/Krishmal2004/Ecommerce.git
cd Ecommerce
```

2. **Configure environment variables**
```bash
# Create application.properties or .env file
GEMINI_API_KEY=your_gemini_api_key
STRIPE_SECRET_KEY=your_stripe_secret_key
STRIPE_PUBLIC_KEY=your_stripe_public_key
GOOGLE_CLIENT_ID=your_google_client_id
GOOGLE_CLIENT_SECRET=your_google_client_secret
GITHUB_CLIENT_ID=your_github_client_id
GITHUB_CLIENT_SECRET=your_github_client_secret
SMTP_HOST=smtp.gmail.com
SMTP_PORT=587
SMTP_USERNAME=your_email@gmail.com
SMTP_PASSWORD=your_app_password
```

3. **Build and run the application**
```bash
mvn clean install
mvn spring-boot:run
```

4. **Access the application**
```
Open your browser and navigate to: http://localhost:8080
```

## 📖 Usage

1. **Sign Up/Login**: Use Google or GitHub OAuth2 to authenticate
2. **Add Transactions**: Record your income and expenses
3. **View Analytics**: Access AI-powered insights on your spending patterns
4. **Get Forecasts**:  Receive budget predictions based on your financial history
5. **Process Payments**: Use Stripe integration for secure transactions
6. **Receive Confirmations**: Get automated email notifications for important actions

## 🧠 AI Integration

The Gemini 2.5 integration provides:

- **Natural Language Insights**: Ask questions about your finances in plain English
- **Spending Pattern Analysis**: Understand where your money goes
- **Smart Categorization**: Automatic transaction categorization
- **Predictive Budgeting**: AI-driven forecasts for future expenses
- **Personalized Recommendations**: Tailored financial advice

## 🔒 Security

- OAuth2 authentication for secure social login
- Encrypted data transmission
- PCI-compliant payment processing via Stripe
- Secure API key management
- Session management with Spring Security

## 🛠️ Project Structure

```
Ecommerce/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/wealthwise/
│   │   │       ├── config/
│   │   │       ├── controller/
│   │   │       ├── model/
│   │   │       ├── repository/
│   │   │       ├── service/
│   │   │       └── WealthWiseApplication.java
│   │   └── resources/
│   │       ├── static/
│   │       ├── templates/
│   │       └── application.properties
│   └── test/
├── pom.xml
└── README.md
```

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request. 

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

**Krishmal2004**
- GitHub: [@Krishmal2004](https://github.com/Krishmal2004)

## 🙏 Acknowledgments

- Google Gemini AI for powering the analytics engine
- Stripe for secure payment processing
- Spring Boot community for excellent documentation
- All contributors who help improve this project

## 📧 Contact

For questions or feedback, please reach out via GitHub issues or email.

---

**Built with ❤️ using Java, Spring Boot, and Gemini 2.5 AI**

#Java #SpringBoot #GeminiAI #Stripe #FullStack #FinTech #GenerativeAI
