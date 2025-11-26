import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
from sklearn.model_selection import train_test_split, GridSearchCV
from sklearn.ensemble import RandomForestClassifier
from sklearn.linear_model import LogisticRegression
from sklearn.tree import DecisionTreeClassifier
from sklearn.preprocessing import StandardScaler
from sklearn.metrics import classification_report, accuracy_score
import joblib

# Załaduj dane z pliku CSV
data = pd.read_csv('datasets/Dataset.csv')

# Wyświetl podstawowe informacje o danych
print("Podgląd danych:")
print(data.head())
print("\nKolumny w zestawie danych:", data.columns)

# Wybierz cechy (features) i etykiety (labels)
X = data.drop(columns=['Type'])  # Wszystkie kolumny poza 'Type' to cechy
y = data['Type']  # Kolumna 'Type' jako etykieta (0 = bezpieczny, 1 = phishing)

# Skalowanie cech
scaler = StandardScaler()
X_scaled = scaler.fit_transform(X)

# Podział na dane treningowe i testowe
X_train, X_test, y_train, y_test = train_test_split(X_scaled, y, test_size=0.2, random_state=42)

# Zapis danych do folderu 'datasets'
pd.DataFrame(X_train).to_csv("datasets/trainDataset/X_train.csv", index=False)
pd.DataFrame(X_test).to_csv("datasets/testDataset/X_test.csv", index=False)
pd.DataFrame(y_train).to_csv("datasets/trainDataset/y_train.csv", index=False)
pd.DataFrame(y_test).to_csv("datasets/testDataset/y_test.csv", index=False)

# Modele do porównania
models = {
    'RandomForest': RandomForestClassifier(random_state=42),
    'DecisionTree': DecisionTreeClassifier(random_state=42),
    'LogisticRegression': LogisticRegression(random_state=42)
}

# Wyniki modeli
results = {}

for model_name, model in models.items():
    model.fit(X_train, y_train)
    y_pred = model.predict(X_test)
    accuracy = accuracy_score(y_test, y_pred)
    results[model_name] = accuracy
    print(f"\n{model_name} - Raport klasyfikacji:")
    print(classification_report(y_test, y_pred))

# Wybór najlepszego modelu
best_model_name = max(results, key=results.get)
print(f"\nNajlepszy model: {best_model_name} z dokładnością {results[best_model_name]:.2f}")

# Optymalizacja najlepszego modelu za pomocą GridSearchCV
if best_model_name == 'RandomForest':
    param_grid = {
        'n_estimators': [50, 100, 150],
        'max_depth': [None, 10, 20],
        'min_samples_split': [2, 5, 10]
    }
    best_model = RandomForestClassifier(random_state=42)
elif best_model_name == 'DecisionTree':
    param_grid = {
        'max_depth': [None, 10, 20],
        'min_samples_split': [2, 5, 10]
    }
    best_model = DecisionTreeClassifier(random_state=42)
elif best_model_name == 'LogisticRegression':
    param_grid = {
        'C': [0.1, 1, 10],
        'solver': ['lbfgs', 'saga']
    }
    best_model = LogisticRegression(random_state=42, max_iter=1000)

grid_search = GridSearchCV(best_model, param_grid, cv=5, scoring='accuracy', n_jobs=-1)
grid_search.fit(X_train, y_train)
optimized_model = grid_search.best_estimator_
print(f"\nNajlepsze parametry dla {best_model_name}: {grid_search.best_params_}")

# Zapisanie najlepszego modelu
target_file = f"optimized_{best_model_name.lower()}_model.pkl"
joblib.dump(optimized_model, target_file)
print(f"\nZoptymalizowany model zapisany jako '{target_file}'.")

# Finalna ocena zoptymalizowanego modelu
y_pred_optimized = optimized_model.predict(X_test)
print(f"\nZoptymalizowany {best_model_name} - Raport klasyfikacji:")
print(classification_report(y_test, y_pred_optimized))

# Wykresy różnic między wiadomościami phishingowymi a prawdziwymi
phishing = data[data['Type'] == 1]
legitimate = data[data['Type'] == 0]

# Wykres 1: Porównanie liczby phishingowych i prawdziwych wiadomości
sns.countplot(x='Type', data=data)
plt.title('Rozkład phishingowych i prawdziwych wiadomości')
plt.xticks([0, 1], ['Legitimate', 'Phishing'])
plt.xlabel('Typ wiadomości')
plt.ylabel('Liczba')
plt.savefig("datasets/diagrams/message_distribution.png")
plt.show()

# Wykres 2: Średnia wartości cech dla każdej grupy
feature_means = data.groupby('Type').mean().T
feature_means.plot(kind='bar', figsize=(12, 6))
plt.title('Średnie wartości cech dla phishingowych i prawdziwych wiadomości')
plt.xlabel('Cechy')
plt.ylabel('Średnia wartość')
plt.savefig("datasets/diagrams/feature_means.png")
plt.show()
