from qdrant_client import QdrantClient
import ollama

# Qdrant client
qdrant = QdrantClient(url="http://localhost:6333")

# Collection name
COLLECTION = "api_test_patterns"

def get_embedding(text: str):
    return ollama.embeddings(
        model="nomic-embed-text",
        prompt=text
    )["embedding"]

def retrieve_patterns(path: str, k: int = 5):
    # Lightweight, stable retrieval query
    query_text = f"API endpoint path: {path}"

    query_vec = get_embedding(query_text)

    result = qdrant.query_points(
        collection_name=COLLECTION,
        query=query_vec,
        limit=k
    )

    return [point.payload for point in result.points]
