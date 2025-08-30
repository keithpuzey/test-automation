import json
import csv
import requests
from config import workspaceID, BMCredentials, test_data_csv

# Print the value
print(f"BM Creds: {BMCredentials}")

class CSVDataGeneration:
    def __init__(self, datamodel_path, repeat_count):
        self.datamodel_path = datamodel_path
        self.repeat_count = repeat_count

    def generate_test_data(self):
        print("DEBUG types:", type(json), type(csv), type(requests), type(str))
        try:
            # Open Data Model file
            with open(self.datamodel_path, 'r', encoding='utf-8') as datamodel_file:
                datamodel_def = json.load(datamodel_file)

            # Update repeat count in Data Model
            default_obj = datamodel_def['data']['attributes']['model']['entities']['default']
            default_obj['repeat'] = self.repeat_count

            # Generate Test Data
            url = f"https://tdm.blazemeter.com/api/v1/workspaces/{workspaceID}/testdata/generatefile?entity=default"
            headers = {
                'Content-Type': 'application/json',
                'Accept': 'application/json,text/javascript, */*',
            }

            response = requests.post(
                url,
                json=datamodel_def,
                headers=headers,
                auth=BMCredentials
            )
           print("Response status:", response.status_code)
           print("Response body:", response.text[:500])  # first 500 chars 
            
            response.raise_for_status()

            # Extract the result
            result_data = response.json().get('result', {})
            if result_data:
                csv_content = result_data.get('content', '')
                csv_reader = csv.reader(csv_content.splitlines())

                print("\n--- Generated CSV Data ---")
                for row in csv_reader:
                    print(', '.join(row))
                print("--- End of CSV Data ---\n")

            print(f"{self.repeat_count} Test Data Records generated using Data Model {self.datamodel_path}")

        except Exception as e:
            print(f"An error occurred: {str(e)}")

if __name__ == "__main__":
    import sys
    datamodel_path = sys.argv[1]
    repeat_count = int(sys.argv[2])   # ensure int

    csv_data_generation = CSVDataGeneration(datamodel_path, repeat_count)
    csv_data_generation.generate_test_data()