import {useToast} from 'vue-toastification';
import { customFetch } from "../utils/FetchUtil";

const toast = useToast();

export async function fetchData(matieres, loading, notes, etudiant) {
    try {
        const response = await customFetch('notes/matieres');
        if (!response.ok) {
            throw new Error('Erreur lors de la récupération des matières');
        }
        const data = await response.json();
        const responseNotes = await customFetch(`users/${etudiant.id}/notes`);
        if (!response.ok) {
            throw new Error('Erreur lors de la récupération des notes');
        }
        const dataNotes = await responseNotes.json();
        matieres.value = data;
        loading.value = false;
        notes.value = dataNotes;
    } catch (error) {
        toast.error("Une Erreur est survenue lors de la récupération des données", { position: 'top-center' });
    }
}

export async function fetchMoyenne(etudiant) {
    try {
        //Aller chercher la moyenne
        const response = await customFetch(`notes/moyenne/utilisateur/${etudiant.id}`);
        if (!response.ok) {
            throw new Error('Erreur lors de la récupération de la moyenne - reponse not ok');
        }
        return await response.json();
    } catch (error) {
        toast.error("Une Erreur est survenue lors de la récupération de la moyenne", { position: 'top-center' });
    }
}


export async function exportToCSV() {
    try {
        const response = await customFetch('users/exportCSV', {
            method: 'GET',
            headers: {
                'Content-Type': 'text/csv',
            },
        });
        const blob = await response.blob();
        const url = window.URL.createObjectURL(new Blob([blob]));
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', 'Etudiant.csv');
        document.body.appendChild(link);
        link.click();
        window.URL.revokeObjectURL(url);
    } catch (error) {
        toast.error("Une Erreur est survenue lors de l'exportation des données", { position: 'top-center' });
    }
}

export async function uploadFileS(selectedFile, loading){
    if (selectedFile.value) {
        let formData = new FormData();
        formData.append('file', selectedFile.value);
        loading.value = true;
        customFetch('users/processExcel',{
            method: 'POST',
            body: formData
        })
        .then(response => {
            console.log(response.status)
            location.reload();
        })
        .catch(error => {
          loading.value = false;
          toast.error("Une erreur s'est produite pendant l'envoi du fichier !", { position: 'top-center' });
        });
      } else {
        loading.value = false;
        toast.error("Vous n'avez pas sélectionné de fichier à envoyer !", { position: 'top-center' })
      }
}