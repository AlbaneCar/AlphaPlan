import {useToast} from 'vue-toastification';
import { customFetch } from '../utils/FetchUtil';

// On utilise useToast pour afficher des messages de succès ou d'erreur
const toast = useToast();

/**
 * @brief Exporter les notes des sprints au format Excel
 * @return {Promise<void>}
 * @author Hugo BOURDAIS
 * @date 30/04/2024
 */
export async function exportXLSX() {
    try {
        // Récupérer les notes des sprints
        const response = await customFetch(`export/notes-sprints`, {
            headers: {
                'Content-Type': 'application/vnd.ms-excel',
            },
        });
        if (response.ok) {
            // Obtenir le contenu du fichier Excel sous forme de blob
            const blob = await response.blob();

            // Créer un objet URL pour le fichier Excel
            const url = URL.createObjectURL(blob);

            // Créer un lien de téléchargement
            const downloadLink = document.createElement('a');
            downloadLink.href = url;
            downloadLink.download = 'notes_sprints.xlsx'; // Nom du fichier Excel

            // Déclencher le téléchargement du fichier
            downloadLink.click();

            // Supprimer l'URL de l'objet Blob pour libérer de la mémoire
            URL.revokeObjectURL(url);

            // Afficher un message de succès
            toast.success('Notes exportées avec succès !', {position : 'top-center'});
        } else {

            // On récupère le message d'erreur de l'API
            const blob = await response.blob();
            const error = await blob.text();

            // Afficher un message d'erreur
            toast.error(error + ' !', {position : 'top-center'});
            throw new Error('Erreur lors de l\'export des notes !');
        }
    } catch (error) {

        // Afficher un message d'erreur
        toast.error('Erreur lors de l\'export des notes !', {position : 'top-center'});
        throw error;
    }
}