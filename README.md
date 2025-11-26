# Phishing-detecting-app # SpamCheck
engineering project

## 📍 Overview  
The **SpamCheck** project is a comprehensive system designed to detect phishing messages in both **email** and **SMS** formats. This project consists of three main modules: a **machine learning model** for text classification, a **REST API** for serving the model, and a **mobile Android application** built with Kotlin, enabling users to easily check if a message is phishing or not. The system works efficiently with a response time of less than **500ms**.

---

## 💼 **Project Details**

### **Main Features:**
- **AI-based Classification:** The system uses machine learning models, such as **Naive Bayes** and **MLP**, to classify emails and SMS as either legitimate ("ham") or phishing ("spam").
- **Mobile Application:** Built in **Kotlin**, it allows users to input email or SMS content and checks whether the message is legitimate or phishing.
- **REST API:** The server-side part is implemented using **FastAPI** and serves the classification model via a POST request, providing results with confidence scores.
- **Efficiency:** The application is designed to be lightweight, offering **quick response times** for checking messages.

---

## 💻 **Technologies Used**

### **Machine Learning (ML):**
- **Python**
  - **NLTK**: Natural language processing (NLP) library used for tokenization and preprocessing.
  - **Scikit-learn**: Library for building and evaluating the ML models (Naive Bayes, MLP).
  - **Pandas**, **Numpy**: For data manipulation and processing.
  - **Joblib**: For saving the trained ML model.
  - **Deep_Translator**: For automating text translations when needed.

### **Backend:**
- **FastAPI**: Framework for creating the REST API, ensuring fast, efficient communication with the mobile app.
- **Uvicorn**: A lightning-fast ASGI server to run the FastAPI application.

### **Frontend (Mobile Application):**
- **Kotlin**: The primary language for building the Android app.
- **Jetpack Compose**: For building modern, responsive UI in Android.

---

## 🚀 **How It Works**

### **Text Classification Model**
1. **Data Collection**: A dataset of over 2000 examples of each class (ham and spam) is collected for training the model.
2. **Preprocessing**: The data undergoes tokenization, removal of stop-words, and other text-cleaning operations.
3. **Model Training**: Two machine learning models—**Naive Bayes** and **MLP (Multi-Layer Perceptron)**—are trained to classify the input text. The model with the highest **F1-score** is selected for deployment.
4. **API Endpoint**: The trained model is deployed through a REST API, allowing the mobile app to send messages for classification. The system responds with the label (ham/spam) and confidence score.

### **Mobile Application**
1. **UI**: Users can input email or SMS content into the app and hit a "check" button to classify the message.
2. **Server Communication**: The app sends the content to the FastAPI backend for classification, receives the result, and displays whether the message is phishing or legitimate.
3. **Additional Features**: Users can see a confidence score and statistical graphs for phishing detection.

---

## 📚 **Installation**

### **Dependencies:**
- **Python 3.8+**
- **Kotlin** (for Android development)
- **Android Studio**
- **Scikit-learn**, **FastAPI**, **Uvicorn** for Python dependencies
- **Jetpack Compose** for Kotlin UI development

### **Setting Up the Backend:**
1. Install the necessary Python dependencies:
   ```bash
   pip install fastapi scikit-learn pandas numpy uvicorn
