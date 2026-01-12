import json
import os

CACHE_FILE = "cache/testcase_cache.json"

def load_cache():
    if not os.path.exists(CACHE_FILE):
        return {}
    with open(CACHE_FILE, "r", encoding="utf-8") as f:
        return json.load(f)

def save_cache(cache):
    os.makedirs("cache", exist_ok=True)
    with open(CACHE_FILE, "w", encoding="utf-8") as f:
        json.dump(cache, f, indent=2)

def get_cached(signature):
    cache = load_cache()
    return cache.get(signature)

def store_cached(signature, data):
    cache = load_cache()
    cache[signature] = data
    save_cache(cache)
