from sklearn.metrics.pairwise import cosine_similarity
import numpy as np

def retrieve_relevant_endpoints(query, chunks, top_k=5):
    # Embed the query
    query_embedding = openai.Embedding.create(
        model="text-embedding-3-large",
        input=query
    )["data"][0]["embedding"]

    # Compute similarities
    sims = [cosine_similarity([query_embedding], [chunk["embedding"]])[0][0] for chunk in chunks]
    
    # Return top-k
    idx = np.argsort(sims)[-top_k:][::-1]
    return [chunks[i] for i in idx]
