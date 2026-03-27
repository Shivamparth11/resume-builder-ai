from email.mime import text

from flask import Flask, request, jsonify
import nltk
import spacy
from nltk.corpus import stopwords
from nltk.tokenize import word_tokenize
import language_tool_python

# Download required resources if not already done
nltk.download('punkt')
nltk.download('stopwords')

app = Flask(__name__)
nlp = spacy.load("en_core_web_sm")

STOP_WORDS = set(stopwords.words('english'))

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

ACTION_VERBS = ["develop","developed",
"build","built",
"design","designed",
"implement","implemented",
"optimize","optimized",
"create","created",
"lead","led",
"manage","managed",
"improve","improved",
"engineer","engineered",
"deploy","deployed",
"automate","automated",
"collaborate","collaborated",
"work","worked",
"integrate","integrated"]

def calculate_skill_score(words):
    text = " ".join(words)
    matched = []
    for skill in TECH_KEYWORDS:
       if f" {skill} " in f" {text} ":
          matched.append(skill)
    score = min(len(set(matched)) * 5, 100)
    return score, matched

def calculate_ats_score(words):
    tech_matches = [w for w in words if w in TECH_KEYWORDS]
    unique_skills = len(set(tech_matches))

    score = min(unique_skills * 10, 100)
    return score

tool = language_tool_python.LanguageTool('en-US')

def grammar_score(text):
    matches = tool.check(text)
    errors = len(matches)

    if errors == 0:
        return 100

    score = max(100 - errors * 10, 0)
    return score

def improvement_suggestions(words, verbs):
    suggestions = []

    if len(words) < 12:
        suggestions.append("Summary is too short. Add more details.")

    if len(verbs) < 2:
        suggestions.append("Use more action verbs like developed, built, implemented.")

    tech = [w for w in words if w in TECH_KEYWORDS]

    if len(tech) < 2:
        suggestions.append("Add more technical skills to improve ATS ranking.")

    return suggestions

@app.route('/analyze', methods=['POST'])
def analyze():
    data = request.get_json()
    summary = data.get('summary', '')

    if not summary.strip():
        return jsonify({'feedback': '⚠️ Summary is empty. Please enter a valid summary.'})

    # Tokenize and clean
    tokens = word_tokenize(summary)
    words = [w.lower() for w in tokens if w.lower() not in STOP_WORDS and w.isalpha()]

    # POS tagging with spaCy
    doc = nlp(summary)
    verbs = [token.lemma_ for token in doc if token.pos_ == 'VERB']

    # Matching
    
    matched_verbs = [v for v in verbs if v in ACTION_VERBS]
    
    #AI SERVICES
    skill_score, skills = calculate_skill_score(words)
    ats_score = calculate_ats_score(words)
    grammar = grammar_score(summary)
    suggestions = improvement_suggestions(words, verbs)

    feedback = []

    # Word length check
    if len(words) < 8:
        feedback.append("❗ Summary is too short. Add more details about your skills or achievements.")
    else:
        feedback.append("✅ Summary length is good.")

    # Tech keyword check
    # Tech keyword check
    if skills:
        feedback.append(f"🔧 Detected tech skills: {', '.join(skills)}.")
    else:
        feedback.append("❗ No key technical skills detected. Add technologies like Java, Spring, MySQL etc.")

    # Action verbs check
    if matched_verbs:
        feedback.append(f"💪 Good use of action verbs: {', '.join(set(matched_verbs))}.")
    else:
        feedback.append("⚠️ Consider adding action verbs like 'developed', 'built', 'implemented'.")

    return jsonify({
    "feedback": " ".join(feedback),
    "skill_score": skill_score,
    "ats_score": ats_score,
    "grammar_score": grammar,
    "skills_detected": skills,
    "action_verbs": matched_verbs,
    "suggestions": suggestions
})

if __name__ == '__main__':
    app.run(port=5000)
