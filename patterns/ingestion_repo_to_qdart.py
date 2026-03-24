from extract_patterns import extract_patterns_from_repo
from qdrant_store import store_pattern
import os

REPO_DIR = "repos"

for repo in os.listdir(REPO_DIR):
    repo_path = os.path.join(REPO_DIR, repo)

    if not os.path.isdir(repo_path):
        continue

    print(f"Extracting patterns from {repo}")

    patterns = extract_patterns_from_repo(repo_path)

    for p in patterns:
        store_pattern(p, repo)
        print(f"Stored pattern: {p[:60]}...")
