import tensorflow as tf
import numpy as np

# Wczytanie modelu TFLite
interpreter = tf.lite.Interpreter(model_path="model.tflite")
interpreter.allocate_tensors()

# Pobranie szczegółów o tensorach wejściowych i wyjściowych
input_details = interpreter.get_input_details()
output_details = interpreter.get_output_details()

# Generowanie losowych danych wejściowych do testowania modelu
# Zakładamy, że model akceptuje wejścia o określonym kształcie
input_shape = input_details[0]['shape']
input_data = np.random.rand(*input_shape).astype(np.float32)

# Przekazanie danych wejściowych do modelu
interpreter.set_tensor(input_details[0]['index'], input_data)

# Uruchomienie modelu
interpreter.invoke()

# Pobranie wyników
output_data = interpreter.get_tensor(output_details[0]['index'])

# Wyświetlenie wyników
print("Przewidywania modelu:", output_data)
