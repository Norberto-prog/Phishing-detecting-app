import tensorflow as tf
import numpy as np
import pandas as pd
from sklearn.preprocessing import StandardScaler
from joblib import load
from tensorflow.python.framework.convert_to_constants import convert_variables_to_constants_v2, \
    convert_variables_to_constants_v2_as_graph

# Wczytanie danych testowych
X_test = pd.read_csv('datasets/testDataset/X_test.csv')

# Wczytanie dopasowanego skalera
scaler = load('scaler.joblib')
X_test_scaled = scaler.transform(X_test)

# Zdefiniowanie uproszczonego modelu Random Forest w TensorFlow
class RandomForestTF(tf.keras.Model):
    def __init__(self, n_trees, n_features):
        super(RandomForestTF, self).__init__()
        self.trees = [
            tf.keras.layers.Dense(1, activation="sigmoid") for _ in range(n_trees)
        ]

    def call(self, inputs):
        outputs = tf.concat([tree(inputs) for tree in self.trees], axis=1)
        final_output = tf.reduce_mean(outputs, axis=1, keepdims=True)
        return final_output

# Liczba cech i drzew
n_trees = 10
n_features = X_test.shape[1]

# Utwórz model TensorFlow
rf_model = RandomForestTF(n_trees=n_trees, n_features=n_features)

# Definiowanie klasy do opakowania modelu
class RandomForestWrapper(tf.Module):
    def __init__(self, model):
        self.model = model

    @tf.function(input_signature=[tf.TensorSpec(shape=[None, n_features], dtype=tf.float32)])
    def predict(self, inputs):
        predictions = self.model(inputs)
        return predictions

# Utworzenie opakowanego modelu
wrapped_model = RandomForestWrapper(rf_model)

# Zapis modelu TensorFlow
saved_model_dir = "saved_model_rf"
tf.saved_model.save(wrapped_model, saved_model_dir)

# Zamrożenie grafu
concrete_func = wrapped_model.predict.get_concrete_function()
frozen_func, frozen_graph_def = convert_variables_to_constants_v2_as_graph(concrete_func)

# Zapis grafu jako .pb
tf.io.write_graph(frozen_graph_def, ".", "frozen_model.pb", as_text=False)
print("Zamrożono graf modelu.")

# Konwersja do formatu TFLite z zamrożonym grafem
converter = tf.lite.TFLiteConverter.from_saved_model(saved_model_dir)
converter.optimizations = [tf.lite.Optimize.DEFAULT]
tflite_model = converter.convert()

# Testowanie zamrożonego modelu
inputs = np.random.rand(1, n_features).astype(np.float32)
predictions = wrapped_model.predict(tf.convert_to_tensor(inputs))
print("Testowanie modelu zakończone sukcesem:", predictions)


# Zapisanie modelu TFLite do pliku
with open('model.tflite', 'wb') as f:
    f.write(tflite_model)

print("Model został zamrożony i zapisany jako TensorFlow Lite.")
