"""
Vector storage pipeline for API test & design patterns
------------------------------------------------------
• Embeddings: Ollama (nomic-embed-text)
• Vector DB: Qdrant
• Distance: Cosine
• Vector size: 768
"""

from qdrant_client import QdrantClient
from qdrant_client.http.models import VectorParams, Distance
import ollama
import uuid
from typing import List


# -------------------------------
# 1. Qdrant Connection
# -------------------------------
qdrant = QdrantClient(
    url="http://localhost:6333",
    timeout=30
)

COLLECTION_NAME = "api_test_patterns"
VECTOR_SIZE = 768


# -------------------------------
# 2. Ensure Collection Exists
# -------------------------------
def ensure_collection():
    """
    Creates the collection only if it does not already exist.
    Prevents accidental data loss.
    """
    if not qdrant.collection_exists(COLLECTION_NAME):
        qdrant.create_collection(
            collection_name=COLLECTION_NAME,
            vectors_config=VectorParams(
                size=VECTOR_SIZE,
                distance=Distance.COSINE
            )
        )
        print(f" Created collection: {COLLECTION_NAME}")
    else:
        print(f" Collection already exists: {COLLECTION_NAME}")


# -------------------------------
# 3. Text → Vector Embedding
# -------------------------------
def embed_text(text: str) -> List[float]:
    """
    Converts text into a 768-dimensional vector using Ollama.
    """
    response = ollama.embeddings(
        model="nomic-embed-text",
        prompt=text
    )
    return response["embedding"]


# -------------------------------
# 4. Store Pattern in Qdrant
# -------------------------------
def store_pattern(
    pattern_text: str,
    source_repo: str,
    file_path: str | None = None
):
    """
    Stores a design/test pattern into Qdrant with metadata.
    """
    vector = embed_text(pattern_text)

    qdrant.upsert(
        collection_name=COLLECTION_NAME,
        points=[
            {
                "id": str(uuid.uuid4()),
                "vector": vector,
                "payload": {
                    "pattern_text": pattern_text,
                    "source_repo": source_repo,
                    "file_path": file_path
                }
            }
        ]
    )

