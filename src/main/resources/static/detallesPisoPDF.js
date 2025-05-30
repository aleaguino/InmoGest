document.addEventListener('DOMContentLoaded', function() {
    const btn = document.getElementById('descargarPDF');
    if (!btn) return;
    btn.addEventListener('click', function() {
        const mainContent = document.querySelector('.main-content');
        if (!mainContent) return;
        if (typeof html2canvas === 'undefined' || typeof jsPDF === 'undefined') {
            alert('No se han cargado las librerías necesarias para exportar a PDF.');
            return;
        }
        html2canvas(mainContent, { scale: 2, useCORS: true }).then(canvas => {
            const imgData = canvas.toDataURL('image/png');
            const pdf = new jsPDF('p', 'mm', 'a4');
            const pageWidth = pdf.internal.pageSize.getWidth();
            const imgProps = pdf.getImageProperties(imgData);
            const pdfWidth = pageWidth - 20;
            const pdfHeight = (imgProps.height * pdfWidth) / imgProps.width;
            pdf.addImage(imgData, 'PNG', 10, 10, pdfWidth, pdfHeight);
            pdf.save('detalles-piso.pdf');
        });
    });
});
