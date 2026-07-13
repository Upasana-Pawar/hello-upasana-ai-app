import os
import re
import json
import base64
from flask import Flask, render_template, request, jsonify

app = Flask(__name__)

# Security Screen utility functions
def scrub_pii(text: str) -> str:
    if not text:
        return ""
    # SSN pattern: XXX-XX-XXXX
    ssn_pattern = r'\b\d{3}-\d{2}-\d{4}\b'
    # Credit Card pattern: 16 digits with optional spaces/dashes or groups of 4
    cc_pattern = r'\b(?:\d{4}[-\s]?){3}\d{4}\b|\b\d{16}\b'
    
    scrubbed = re.sub(ssn_pattern, '[REDACTED_SSN]', text)
    scrubbed = re.sub(cc_pattern, '[REDACTED_CC]', scrubbed)
    return scrubbed

def detect_prompt_injection(text: str) -> bool:
    if not text:
        return False
    lowered = text.lower()
    injection_keywords = [
        "bypass", 
        "override", 
        "ignore instruction", 
        "ignore previous", 
        "force approval", 
        "force auto-approval", 
        "auto-approve", 
        "system prompt", 
        "bypass rule",
        "override guardrail",
        "admin mode"
    ]
    return any(kw in lowered for kw in injection_keywords)

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

# Endpoint 1 & 2: Pub/Sub Webhook with Base64 Decoding & Integrated Security Screen
@app.route('/apps/expense_agent/trigger/pubsub', methods=['POST'])
def trigger_pubsub():
    payload = request.get_json(silent=True) or {}
    
    # Extract subscription ID for clean context isolation
    subscription_id = payload.get('subscription_id', 'sub-expense-agent-default')
    
    # Support standard PubSub payload format where data is in message.data
    message_data_b64 = ""
    if 'message' in payload and isinstance(payload['message'], dict):
        message_data_b64 = payload['message'].get('data', '')
    else:
        message_data_b64 = payload.get('data', '')
        
    if not message_data_b64:
        return jsonify({
            "status": "ERROR",
            "message": "Missing Base64 encoded 'data' in payload."
        }), 400

    try:
        # Decode Base64 string
        decoded_bytes = base64.b64decode(message_data_b64)
        decoded_str = decoded_bytes.decode('utf-8')
        try:
            data_obj = json.loads(decoded_str)
            description = data_obj.get('description', '')
            amount = data_obj.get('amount', '0.00')
        except Exception:
            description = decoded_str
            amount = "Unknown"
    except Exception as e:
        return jsonify({
            "status": "ERROR",
            "message": f"Failed to decode Base64 data: {str(e)}"
        }), 400

    # 1. RUN SECURITY SCREEN MIDDLEWARE
    pii_scrubbed_desc = scrub_pii(description)
    is_injection = detect_prompt_injection(description)

    if is_injection:
        # SHORT CIRCUIT: Flag security event, route to human queue
        return jsonify({
            "status": "SECURITY_EVENT",
            "route": "Human-In-The-Loop review queue",
            "flagged": True,
            "security_type": "PROMPT_INJECTION",
            "subscription_id": subscription_id,
            "original_payload_preview": description[:100],
            "scrubbed_description": pii_scrubbed_desc,
            "amount": amount,
            "message": "CRITICAL: Prompt injection attempt blocked! Flow redirected to manual security queue."
        })

    # Normal success path with scrubbed payload
    return jsonify({
        "status": "SUCCESS",
        "route": "Primary LLM Reviewer Node",
        "flagged": False,
        "subscription_id": subscription_id,
        "scrubbed_description": pii_scrubbed_desc,
        "amount": amount,
        "message": "Security checks passed. PII successfully sanitized."
    })

# Expanded Endpoint for Day 4: Webhook simulator with Session ID Isolation
@app.route('/api/agent/trigger/pubsub', methods=['POST'])
def trigger_agent_pubsub():
    payload = request.get_json(silent=True) or {}
    
    # Extract session ID for clean context isolation
    session_id = payload.get('session_id', 'session-default-day4')
    
    # Support standard PubSub payload format where data is in message.data or directly in payload
    message_data_b64 = ""
    if 'message' in payload and isinstance(payload['message'], dict):
        message_data_b64 = payload['message'].get('data', '')
    else:
        message_data_b64 = payload.get('data', '')
        
    if not message_data_b64:
        return jsonify({
            "status": "ERROR",
            "message": "Missing Base64 encoded 'data' parameter in payload."
        }), 400

    try:
        # Decode Base64 string
        decoded_bytes = base64.b64decode(message_data_b64)
        decoded_str = decoded_bytes.decode('utf-8')
        try:
            data_obj = json.loads(decoded_str)
            description = data_obj.get('description', '')
            amount = data_obj.get('amount', '0.00')
        except Exception:
            description = decoded_str
            amount = "Unknown"
    except Exception as e:
        return jsonify({
            "status": "ERROR",
            "message": f"Failed to decode Base64 data: {str(e)}"
        }), 400

    # 1. RUN SECURITY SCREEN MIDDLEWARE
    pii_scrubbed_desc = scrub_pii(description)
    is_injection = detect_prompt_injection(description)

    if is_injection:
        # SHORT CIRCUIT: Flag security event, route to human queue
        return jsonify({
            "status": "SECURITY_EVENT",
            "route": "Human-In-The-Loop review queue",
            "flagged": True,
            "security_type": "PROMPT_INJECTION",
            "session_id": session_id,
            "original_payload_preview": description[:100],
            "scrubbed_description": pii_scrubbed_desc,
            "amount": amount,
            "message": "CRITICAL: Prompt injection attempt blocked! Flow redirected to manual security queue."
        })

    # Normal success path with scrubbed payload
    return jsonify({
        "status": "SUCCESS",
        "route": "Primary LLM Reviewer Node",
        "flagged": False,
        "session_id": session_id,
        "scrubbed_description": pii_scrubbed_desc,
        "amount": amount,
        "message": "Security checks passed. PII successfully sanitized."
    })

# Direct checkpoint endpoint for manual test UI
@app.route('/api/security_checkpoint', methods=['POST'])
def security_checkpoint():
    payload = request.get_json(silent=True) or {}
    description = payload.get('description', '')
    
    scrubbed = scrub_pii(description)
    is_injection = detect_prompt_injection(description)
    
    if is_injection:
        return jsonify({
            "status": "SECURITY_EVENT",
            "route": "Human-In-The-Loop Review Queue",
            "scrubbed_description": scrubbed,
            "message": "Blocked: Prompt injection attempt detected!"
        })
        
    return jsonify({
        "status": "SUCCESS",
        "route": "Primary LLM Reviewer",
        "scrubbed_description": scrubbed,
        "message": "Clear: No injections found. PII sanitized."
    })

# Local Gating: Self-Correction Demo Endpoint
@app.route('/api/self_correct', methods=['POST'])
def self_correct():
    payload = request.get_json(silent=True) or {}
    action = payload.get('action', 'scan')
    
    # We demonstrate scanning a file and automatically correcting a credential
    vulnerable_code = 'AWS_SECRET_KEY = "AKIAIOSFODNN7EXAMPLE"'
    remediated_code = 'AWS_SECRET_KEY = os.getenv("AWS_SECRET_KEY")'
    
    if action == 'scan':
        return jsonify({
            "status": "FAILED",
            "violation": "Hardcoded AWS Credentials (Semgrep Rule: credential-in-code)",
            "file": "config/aws.py",
            "code": vulnerable_code,
            "remediable": True
        })
    elif action == 'correct':
        return jsonify({
            "status": "GREEN",
            "violation": "None (Remediated)",
            "file": "config/aws.py",
            "code": remediated_code,
            "message": "Automated self-correction applied! Replaced hardcoded secret with os.getenv environment injection."
        })
        
    return jsonify({"error": "Invalid action"})

if __name__ == '__main__':
    port = int(os.environ.get('PORT', 5000))
    app.run(host='0.0.0.0', port=port, debug=True)
