from flask import Flask, request, jsonify
import nltk
import spacy
from nltk.corpus import stopwords
from nltk.tokenize import word_tokenize

# Download required resources if not already done
nltk.download('punkt')
nltk.download('stopwords')

app = Flask(__name__)
nlp = spacy.load("en_core_web_sm")

# Tech keywords and action verbs to search for
TECH_KEYWORDS = [
    'java', 'python', 'c', 'c++', 'javascript', 'typescript', 'kotlin', 'swift', 'ruby', 'go', 'rust', 'php',
    'html', 'css', 'react', 'angular', 'vue', 'nodejs', 'express', 'nextjs', 'tailwind', 'bootstrap',
    'spring', 'spring boot', 'django', 'flask', 'fastapi', 'laravel', '.net', 'hibernate', 'jpa',
    'mysql', 'postgresql', 'mongodb', 'mariadb', 'oracle', 'sqlite', 'redis', 'firestore', 'dynamodb',
    'aws', 'azure', 'gcp', 'docker', 'kubernetes', 'jenkins', 'terraform', 'git', 'github', 'gitlab', 'ci/cd',
    'machine learning', 'deep learning', 'nlp', 'computer vision', 'pytorch', 'tensorflow', 'sklearn',
    'transformers', 'huggingface',
    'pandas', 'numpy', 'matplotlib', 'seaborn', 'powerbi', 'tableau', 'excel', 'data analysis',
    'data visualization',
    'android', 'ios', 'react native', 'flutter', 'xamarin',
    'dsa', 'oops', 'system design', 'multithreading', 'rest api', 'microservices', 'design patterns', 'mvc',
    'linux', 'windows', 'unix', 'bash', 'shell scripting', 'vs code', 'intellij', 'eclipse',
    'cybersecurity', 'penetration testing', 'selenium', 'junit', 'testng', 'postman', 'soapui',
    'agile', 'scrum', 'jira', 'confluence', 'figma', 'adobe xd', 'api development', 'resume parsing',
    'chatbot', 'prompt engineering',
    'frontend', 'backend', 'fullstack', 'devops', 'ml', 'ai', 'ds', 'qa', 'automation', 'webdev', 'appdev'
]

ACTION_VERBS = ['built', 'developed', 'led', 'created', 'implemented', 'designed']

@app.route('/analyze', methods=['POST'])
def analyze():
    data = request.get_json()
    summary = data.get('summary', '')

    if not summary.strip():
        return jsonify({'feedback': '⚠️ Summary is empty. Please enter a valid summary.'})

    # Tokenize and clean
    tokens = word_tokenize(summary)
    words = [w.lower() for w in tokens if w.lower() not in stopwords.words('english') and w.isalpha()]

    # POS tagging with spaCy
    doc = nlp(summary)
    verbs = [token.lemma_ for token in doc if token.pos_ == 'VERB']

    # Matching
    matched_tech = [word for word in words if word in TECH_KEYWORDS]
    matched_verbs = [v for v in verbs if v in ACTION_VERBS]

    feedback = []

    # Word length check
    if len(words) < 8:
        feedback.append("❗ Summary is too short. Add more details about your skills or achievements.")
    else:
        feedback.append("✅ Summary length is good.")

    # Tech keyword check
    if matched_tech:
        feedback.append(f"🔧 Detected tech skills: {', '.join(matched_tech)}.")
    else:
        feedback.append("❗ No key technical skills detected. Add technologies like Java, Spring, MySQL etc.")

    # Action verbs check
    if matched_verbs:
        feedback.append(f"💪 Good use of action verbs: {', '.join(set(matched_verbs))}.")
    else:
        feedback.append("⚠️ Consider adding action verbs like 'developed', 'built', 'implemented'.")

    return jsonify({'feedback': ' '.join(feedback)})

if __name__ == '__main__':
    app.run(port=5000)
