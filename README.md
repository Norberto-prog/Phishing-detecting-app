### **Szczegółowy plan na pierwszy tydzień**


Przykładowy harmonogram pracy (4 tygodnie)
1. Tydzień 1: Przygotowanie środowiska i danych
Skonfiguruj Android Studio i projekt w Kotlinie/Java.
Przygotuj dataset phishingowych e-maili i przeszkol model ML w Pythonie.
Skonwertuj model na TensorFlow Lite.
2. Tydzień 2: Implementacja analizy treści
Zaimplementuj funkcję do wgrywania plików .eml.
Stwórz moduł parsujący e-maile: wyciąganie treści, linków, słów kluczowych.
3. Tydzień 3: Integracja z modelem ML
Wgraj model TensorFlow Lite do aplikacji Android.
Dodaj funkcję klasyfikacji wiadomości (bezpieczne/phishingowe).
4. Tydzień 4: Interfejs użytkownika i testowanie
Zaprojektuj prosty interfejs:
Ekran główny: przycisk „Wgraj e-mail”.
Ekran wyników: lista podejrzanych elementów.
5. Przetestuj aplikację na emulatorze i urządzeniach fizycznych.


# C:\Users\norbi\AppData\Local\Programs\Python\Python312\python --version


#### **Cel tygodnia:**
1. Przygotowanie środowiska programistycznego.
2. Pobranie i przygotowanie danych (dataset phishingowych e-maili).
3. Stworzenie modelu uczenia maszynowego na komputerze i skonwertowanie go na TensorFlow Lite.

---

### **Dzień 1: Przygotowanie środowiska**
#### **Kroki:**
1. **Zainstaluj Android Studio:**
   - Pobierz i zainstaluj [Android Studio](https://developer.android.com/studio).
   - Utwórz nowy projekt:
     - Typ projektu: „Empty Activity”.
     - Język: Kotlin (lub Java, jeśli preferujesz).
     - Minimalna wersja SDK: Android 8.0 (API 26) lub wyższa.

2. **Zainstaluj narzędzia ML na komputerze:**
   - Upewnij się, że masz zainstalowany Python oraz biblioteki:
     ```bash
     pip install numpy pandas scikit-learn tensorflow
     ```
   - Dodatkowe narzędzia:
     - `Jupyter Notebook` (opcjonalnie, do analizy danych).

3. **Skonfiguruj środowisko TensorFlow Lite w Android Studio:**
   - Dodaj zależność w `build.gradle` projektu:
     ```gradle
     implementation 'org.tensorflow:tensorflow-lite:2.12.0'
     ```

4. **Testowe uruchomienie projektu:**
   - Uruchom pustą aplikację na emulatorze lub fizycznym urządzeniu, aby upewnić się, że środowisko działa poprawnie.

---

### **Dzień 2: Pobranie i przygotowanie danych**
#### **Kroki:**
1. **Pobierz dataset phishingowych e-maili:**
   - Możesz użyć gotowych zbiorów danych:
     - [Phishing Corpus](https://monkey.org/~jose/phishing/).
     - [Enron Email Dataset](https://www.cs.cmu.edu/~./enron/).

2. **Wstępne przetwarzanie danych:**
   - Wczytaj dane do Python:
     ```python
     import pandas as pd
     # Załaduj dane z pliku CSV lub innego źródła
     data = pd.read_csv('phishing_dataset.csv')
     print(data.head())
     ```
   - Oczyść dane (np. usuń puste wiersze, specjalne znaki).

3. **Podział danych:**
   - Podziel dane na treści e-maili (`X`) i ich etykiety (`y`):
     ```python
     X = data['email_content']
     y = data['label']  # 0 = bezpieczny, 1 = phishing
     ```

---

### **Dzień 3-4: Trening modelu ML**
#### **Kroki:**
1. **Wstępne przetwarzanie tekstu:**
   - Tokenizacja i usuwanie stop-słów:
     ```python
     from sklearn.feature_extraction.text import CountVectorizer
     vectorizer = CountVectorizer(stop_words='english')
     X_transformed = vectorizer.fit_transform(X)
     ```

2. **Trenowanie modelu:**
   - Użyj prostego klasyfikatora, np. Logistic Regression:
     ```python
     from sklearn.linear_model import LogisticRegression
     from sklearn.model_selection import train_test_split

     X_train, X_test, y_train, y_test = train_test_split(X_transformed, y, test_size=0.2, random_state=42)

     model = LogisticRegression()
     model.fit(X_train, y_train)
     print(f"Accuracy: {model.score(X_test, y_test)}")
     ```

3. **Konwersja modelu na TensorFlow Lite:**
   - Przekształć model do formatu TFLite:
     ```python
     import tensorflow as tf
     # Eksportuj model jako plik TFLite
     tf_model = tf.keras.Sequential([
         tf.keras.layers.Dense(10, activation='relu'),
         tf.keras.layers.Dense(1, activation='sigmoid')
     ])
     tf_model.compile(optimizer='adam', loss='binary_crossentropy', metrics=['accuracy'])
     # Trening modelu TFLite
     tf_model.fit(X_train.toarray(), y_train, epochs=10, batch_size=32)
     # Konwersja
     converter = tf.lite.TFLiteConverter.from_keras_model(tf_model)
     tflite_model = converter.convert()
     with open('model.tflite', 'wb') as f:
         f.write(tflite_model)
     ```

---

### **Dzień 5-6: Testowanie modelu**
#### **Kroki:**
1. **Przetestuj model na danych testowych:**
   - Upewnij się, że model działa poprawnie i osiąga akceptowalną dokładność (minimum 80%).

2. **Przygotuj model do integracji z aplikacją:**
   - Umieść plik `model.tflite` w folderze `assets` projektu Android.

---

### **Dzień 7: Podsumowanie tygodnia**
1. **Zweryfikuj postępy:**
   - Model ML jest gotowy i przetestowany.
   - Projekt Android zawiera odpowiednią konfigurację TensorFlow Lite.

2. **Plan na kolejny tydzień:**
   - Implementacja parsowania plików `.eml`.
   - Integracja modelu ML z aplikacją.

---

Jeśli potrzebujesz pomocy w realizacji któregoś z tych kroków, mogę przygotować szczegółowy kod, wyjaśnienia lub dostosować plan do Twojego tempa pracy!