Dzień 5-6: Testowanie modelu
Krok 1: Przetestuj model na danych testowych
Załaduj model TFLite:

Użyj tensorflow-lite w Pythonie, aby załadować i przetestować model .tflite.
python
Skopiuj kod
import tensorflow as tf
import numpy as np
import pandas as pd
from sklearn.metrics import accuracy_score

# Wczytanie danych testowych
X_test = pd.read_csv('datasets/testDataset/X_test.csv')
y_test = pd.read_csv('datasets/testDataset/y_test.csv').values.flatten()

# Wczytanie dopasowanego skalera
from joblib import load
scaler = load('scaler.joblib')
X_test_scaled = scaler.transform(X_test)

# Załaduj model TFLite
interpreter = tf.lite.Interpreter(model_path='model.tflite')
interpreter.allocate_tensors()

# Pobierz wejścia i wyjścia modelu
input_details = interpreter.get_input_details()
output_details = interpreter.get_output_details()

# Funkcja do predykcji
def predict_tflite(interpreter, data):
    predictions = []
    for row in data:
        # Ustaw dane wejściowe
        interpreter.set_tensor(input_details[0]['index'], np.expand_dims(row, axis=0).astype(np.float32))
        interpreter.invoke()
        # Pobierz dane wyjściowe
        output = interpreter.get_tensor(output_details[0]['index'])
        predictions.append(output[0][0] > 0.5)  # Zakładamy próg 0.5 dla klasyfikacji binarnej
    return np.array(predictions, dtype=int)

# Predykcje na danych testowych
y_pred = predict_tflite(interpreter, X_test_scaled)

# Oblicz dokładność
accuracy = accuracy_score(y_test, y_pred)
print(f"Dokładność modelu TFLite: {accuracy * 100:.2f}%")

# Sprawdź, czy model spełnia wymóg dokładności
if accuracy >= 0.8:
    print("Model osiąga akceptowalną dokładność.")
else:
    print("Model nie spełnia wymagań dokładności. Wymaga poprawy.")
Krok 2: Przygotowanie modelu do integracji z Androidem
Umieść model w projekcie Androida:

Przenieś plik model.tflite do folderu projektu Android w lokalizacji:
app/src/main/assets/
Instrukcja:

Utwórz folder assets w katalogu app/src/main (jeśli jeszcze nie istnieje).
Skopiuj plik model.tflite do tego folderu.
W systemie plików projekt będzie wyglądał tak:

bash
Skopiuj kod
app/
├── src/
│   ├── main/
│   │   ├── java/  # Kod źródłowy
│   │   ├── res/   # Zasoby aplikacji
│   │   ├── assets/
│   │   │   └── model.tflite
Dodaj TensorFlow Lite do projektu Android:

W pliku build.gradle aplikacji dodaj zależność:

gradle
Skopiuj kod
implementation 'org.tensorflow:tensorflow-lite:2.13.0' // lub aktualna wersja
Synchronizuj projekt, aby pobrać bibliotekę.

Krok 3: Użycie modelu w aplikacji Android
Załaduj model w kodzie Java/Kotlin:

W pliku Java lub Kotlin wczytaj model i wykonaj predykcje.
Przykład w Kotlinie:

kotlin
Skopiuj kod
import org.tensorflow.lite.Interpreter
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

fun loadModelFile(): MappedByteBuffer {
    val assetFileDescriptor = assets.openFd("model.tflite")
    val fileInputStream = FileInputStream(assetFileDescriptor.fileDescriptor)
    val fileChannel = fileInputStream.channel
    val startOffset = assetFileDescriptor.startOffset
    val declaredLength = assetFileDescriptor.declaredLength
    return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
}

fun predict(input: FloatArray): Float {
    val model = Interpreter(loadModelFile())
    val output = Array(1) { FloatArray(1) }
    model.run(input, output)
    return output[0][0]
}
Parametry:
input to tablica danych wejściowych.
predict() zwraca wynik modelu.
Test predykcji w aplikacji:

Wywołaj metodę predict() w logice aplikacji i zweryfikuj wyniki na urządzeniu.