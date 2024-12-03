import { customFetch } from "../utils/FetchUtil";

const baseUrl = import.meta.env.VITE_IP_BACKEND
export async function createSignalement(description, equipeId, utilisateurId) {
    const response = await customFetch('signalements',{
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            description,
            equipeId,
            utilisateurId
        })
    });
}