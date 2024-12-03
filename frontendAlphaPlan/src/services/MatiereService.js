import { customFetch } from '../utils/FetchUtil';

export async function getMatieres() {
    try {
      const response = await customFetch('notes/matieres');
      if (response.ok) {
        return await response.json();
      } else {
        throw new Error('Erreur lors de la récupération des matières');
      }
    } catch (error) {
    console.error('Erreur lors de la récupération des matières avec coefficients :', error);
    throw error;
  }
}

export async function getMatieresWithCoefficients() {
  try {
    const response = await customFetch('notes/matieres');
    const matieres = await response.json();

    const matieresAvecCoefficients = [];
    for (const matiere of matieres) {
      // Vérifier si la matière n'est pas "MOYENNE"
      if (matiere !== "MOYENNE") {
        let coef = null;
        const coefResponse = await customFetch(`notes/${matiere}/coef`);
        try {
          coef = await coefResponse.text();
          if (coef === "/") {
            coef = null;
          }
        } catch (error) {
          console.error('Erreur lors de la récupération du coefficient pour la matière', matiere, ':', error);
        }
        matieresAvecCoefficients.push({ nom: matiere, coefficient: coef !== null ? coef : "/" });
      }
    }

    return matieresAvecCoefficients;
  } catch (error) {
    console.error('Erreur lors de la récupération des matières avec coefficients :', error);
    throw error;
  }
}
