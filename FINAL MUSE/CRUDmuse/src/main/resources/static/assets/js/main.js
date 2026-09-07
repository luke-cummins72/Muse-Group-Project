function myFunction() {
    var x = document.getElementById("myTopnav");
    if (x.className === "navbar") {
        x.className += " responsive";
    } else {
        x.className = "navbar";
    }
}

const togglePassword = document.querySelector('#togglePassword');
const password = document.querySelector('#password');

togglePassword.addEventListener('click', function () {
    // Toggle the type attribute
    const type = password.getAttribute('type') === 'password' ? 'text' : 'password';
    password.setAttribute('type', type);

    // Toggle the eye / eye-slash icon
    this.classList.toggle('fa-eye-slash');
});

function confirmDelete() {
    return confirm("Are you sure you want to delete this project? This action cannot be undone.");
}

function toggleIcon(iconElement) {
    if (iconElement.classList.contains('fa-plus')) {
        // Switch to "check"
        iconElement.classList.remove('fa-plus');
        iconElement.classList.add('fa-check');
    } else if (iconElement.classList.contains('fa-check')) {
        // Switch back to "plus"
        iconElement.classList.remove('fa-check');
        iconElement.classList.add('fa-plus');
    }
}

document.querySelectorAll('.flag-icon-container form').forEach(form => {
    form.addEventListener('submit', function (e) {
        e.preventDefault(); // Prevent default form submission
        const actionUrl = form.getAttribute('action');
        const icon = form.querySelector('i.fa');
        const formData = new FormData(form);

        fetch(actionUrl, {
            method: 'POST',
            body: formData
        })
            .then(response => {
                if (response.ok) {
                    // Toggle the icon dynamically
                    toggleIcon(icon);
                } else {
                    console.error("Failed to update saved projects");
                }
            })
            .catch(error => console.error("Error:", error));
    });
});

function openModal(projectId) {
    document.getElementById('modalProjectId').value = projectId;
    document.getElementById('saveProjectModal').style.display = 'block';
}

function closeModal() {
    document.getElementById('saveProjectModal').style.display = 'none';
    // Clear the fields if necessary
    document.getElementById('interestLevel').value = 'High';
    document.getElementById('notes1').value = '';
    document.getElementById('notes2').value = '';
}

function filterProjects() {
    const selectedInterestLevel = document.getElementById("filterDropdown").value;
    const projects = document.querySelectorAll(".row .col-lg-4");

    projects.forEach((project) => {
        const interestLevel = project.querySelector(".manageButtons p").textContent
            .replace("Interest Level: ", "").trim();

        if (selectedInterestLevel === "All" || interestLevel === selectedInterestLevel) {
            project.style.display = "block";
        } else {
            project.style.display = "none";
        }
    });
}