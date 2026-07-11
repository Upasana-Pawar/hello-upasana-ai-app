import os
from flask import Flask, render_template

app = Flask(__name__)

@app.route('/')
def home():
    data = {
        "developer": "Upasana",
        "day_2_status": "COMPLETED",
        "milestones": [
            "Dynamic Flask Application Build",
            "Custom Dockerfile Architecture Set Up",
            "Model Context Protocol (MCP) Server Integrated"
        ]
    }
    return render_template('index.html', data=data)

if __name__ == '__main__':
    port = int(os.environ.get('PORT', 5000))
    app.run(host='0.0.0.0', port=port, debug=True)
