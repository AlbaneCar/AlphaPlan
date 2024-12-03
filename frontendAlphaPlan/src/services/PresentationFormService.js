import { customFetch } from '../utils/FetchUtil';

export async function getTeamById(teamId) {
  try {
    const response = await customFetch(`users/equipes/${teamId}`);
    return await response.json();
  } catch (error) {
    console.error('Error fetching team data:', error);
    throw error;
  }
}

export async function submitFormData(formData) {
  try {
    const requestOptions = {
      method: 'POST',
      headers: {
          'Content-Type': 'application/json',
      },
      body: JSON.stringify(formData)
    };
    const response = await customFetch(`notification/ajouter`, requestOptions);
    return await response.json();
  } catch (error) {
    console.error('Error submitting form data:', error);
    throw error;
  }
}