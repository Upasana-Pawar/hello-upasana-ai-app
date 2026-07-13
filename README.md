# 🛡️ Secure AI Agent Workspace
> Celebrating learning milestones in the era of **Agentic Engineering** with robust Day 4 Agent Security, Isolation, and Trace Evaluation analytics.
> Built as part of the **5-Day AI Agents: Intensive Vibe Coding Course With Google**.

---

<p align="center">
  <img src="https://img.shields.io/badge/Gemini-3.5%20Flash-blueviolet?style=for-the-badge&logo=google" alt="Gemini" />
  <img src="https://img.shields.io/badge/Google%20ADK-2.0-blue?style=for-the-badge&logo=google" alt="ADK" />
  <img src="https://img.shields.io/badge/Security-PII%20Scrubbing-teal?style=for-the-badge&logo=shield" alt="Security" />
  <img src="https://img.shields.io/badge/Architecture-Deterministic%20Graphs-orange?style=for-the-badge" alt="Architecture" />
</p>

---

## 🎯 1. THE MISSION & CORE GOAL

The overarching objective of the **Secure AI Agent Workspace** is to build a highly secure, enterprise-grade, event-driven (ambient) AI Agent Framework from scratch using **Google ADK 2.0** and **Gemini 3-Pro**. 

The main goal is to transition beyond simple, unstructured "vibe coding" and establish **rigorous, deterministic, and safe engineering boundaries**. Within this framework, autonomous agents are kept fully accountable, ensuring that safety, security, and traceability are baked directly into the agentic runtime lifecycle rather than added as an afterthought.

---

## 💡 2. WHY I CHOSE THIS (The Inspiration)

With the rapid rise of autonomous AI agents, development velocity often outpaces critical security practices. Deploying a highly autonomous agent into an enterprise codebase without strict guardrails introduces massive operational risks, including data leakage, prompt injections, and infinite execution loops. 

I chose to build this project to master the exact intersection of:
- **Agentic Workflow Design:** Organizing agent paths as predictable state graphs.
- **Local AgOps Gating:** Checking payloads and inputs BEFORE they ever reach the model.
- **LLM-as-Judge Evaluation:** Creating automated pipelines that grade execution paths dynamically.

---

## 🚀 3. HOW IT STARTED (The Foundation)

### 📈 Day 1-2: Foundations of Interaction & State
The project began as an interactive chat prototype designed to reliably parse incoming raw text data. It soon progressed into a fully structured, multi-view workflow application using a customized **Tokyo-Night developer interface** (in both Python Flask and Jetpack Compose). 
- **Milestones Reached:** Established the core state graphs, conditional routing nodes, and basic human-in-the-loop (HITL) approval mechanics so that no high-risk action could run without human confirmation.

---

## 🔥 4. HOW IT'S GOING (Where We Are Now)

### 🛡️ Day 3-4: Agent Security, Webhooks, & Trace Evaluation
The workspace has evolved from a simple interactive application into a mature, secure, and ambient event-driven background service with isolated execution contexts.

*   **Pre-Model Security Gateway:** Upgraded with an automated `security_screen` gatekeeper node that redacts PII (e.g. email, API keys) and halts malicious prompt-injection attempts before they can compromise the LLM.
*   **Git Pre-Commit Hook Simulation:** Engineered an active static analysis check utilizing **Semgrep** rules to catch hardcoded secrets. If a secret is leaked, an autonomous self-correction remediation loop fires to refactor the source code to reference environment variables securely.
*   **Trace Evaluation Analytics:** Added a dedicated analytics engine using the **LLM-as-judge** design pattern to evaluate trace trajectories and assign an execution scorecard, securing a perfect **5.0/5.0 trajectory score**.

---

## 🛠️ Tech Stack & Frameworks

| Tool / Framework | Purpose | Description |
| :--- | :--- | :--- |
| **Google ADK 2.0** | Core Runtime | Coordinates the agent state machines and hooks |
| **Gemini Models** | Core LLM Intelligence | Drives high-speed parsing, translation, and evaluations |
| **FastAPI / Flask** | Backend Services | Serves webhooks and runs parallel API-level sanitization |
| **Semgrep** | Static Analysis | Scan tools and workflows for exposed secrets or vulnerabilities |
| **agents-cli** | Developer Tooling | Drives hook setups and developer deployment testing |

---

## 🔮 5. FUTURE OUTLOOK & ROADMAP

The final phase will transition from local workstation verification to full enterprise scale:
- [ ] **Deploy as an Auto-Scaling Serverless Fleet** on Google Cloud Platform.
- [ ] **Implement Multi-Agent Orchestration** utilizing pub/sub networks for decentralized, cooperative task handling.
- [ ] **Real-time Vector Store Integration** for secure, isolated retrieval-augmented generation (RAG) buffers.

---

*Built with passion by **Upasana** during the Google AI Studio 5-Day Intensive Vibe Coding Track. Source code is fully compiled, verified, and ready for deployment.*
