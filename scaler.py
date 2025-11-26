from sklearn.preprocessing import StandardScaler
import pandas as pd
from joblib import dump

# Wczytaj dane treningowe
X_train = pd.read_csv('datasets/testDataset/X_test.csv')

# Dopasowanie skalera do danych treningowych
scaler = StandardScaler()
scaler.fit(X_train)

# Zapisanie dopasowanego skalera
dump(scaler, 'scaler.joblib')
print("Skaler został dopasowany i zapisany jako 'scaler.joblib'.")
