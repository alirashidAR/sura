from qdrant_client import QdrantClient
import ollama

qdrant = QdrantClient(url="http://localhost:6333")

def get_embedding(text):
    return ollama.embeddings(model="nomic-embed-text", prompt=text)["embedding"]

def retrieve_patterns(endpoint, k=5):
    query_text = f"""
    API testing pattern for:
    Method: {endpoint['method']}
    Path: {endpoint['path']}
    Has Body: {bool(endpoint['requestBody'])}
    """
    query_vec = get_embedding(query_text)

    result = qdrant.search(
        collection_name="patterns_collection",
        query_vector=query_vec,
        limit=k
    )

    return [item.payload for item in result]