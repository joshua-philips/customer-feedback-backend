package org.mesika.customerfeedback.services.ticket;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.mesika.customerfeedback.dto.CustomPage;
import org.mesika.customerfeedback.dto.ticket.CommentDTO;
import org.mesika.customerfeedback.dto.ticket.TicketRequest;
import org.mesika.customerfeedback.dto.ticket.TicketResponse;
import org.mesika.customerfeedback.models.auth.ApplicationUser;
import org.mesika.customerfeedback.models.clients.Client;
import org.mesika.customerfeedback.models.tickets.Comment;
import org.mesika.customerfeedback.models.tickets.Ticket;
import org.mesika.customerfeedback.repo.ApplicationUserRepository;
import org.mesika.customerfeedback.repo.ClientRepository;
import org.mesika.customerfeedback.repo.CommentRepository;
import org.mesika.customerfeedback.repo.TicketRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final ModelMapper modelMapper;
    private final TicketRepository ticketRepository;
    private final CommentRepository commentRepository;
    private final ClientRepository clientRepository;
    private final ApplicationUserRepository applicationUserRepository;

    public TicketResponse createTicket(TicketRequest request, UUID clientId, HttpServletRequest servletRequest) {
        Client client;

        if (clientId == null) {
            String source = servletRequest.getRemoteHost();
            client = clientRepository.findBySource(source).orElseThrow();
        } else {
            client = clientRepository.findById(clientId).orElseThrow();
        }

        Ticket ticket = modelMapper.map(request, Ticket.class);
        ticket.setClient(client);

        ticket = ticketRepository.save(ticket);

        return modelMapper.map(ticket, TicketResponse.class);
    }

    public CustomPage<TicketResponse> getAllTickets(int page, int size) {
        Page<Ticket> tickets = ticketRepository
                .findAll(PageRequest.of(page, size, Sort.by("createdDate").descending()));
        return new CustomPage<>(tickets
                .map(t -> modelMapper.map(t, TicketResponse.class)));
    }

    public CustomPage<TicketResponse> getClientTickets(UUID clientId, int page, int size,
            HttpServletRequest servletRequest) {
        UUID useClientId = clientId;
        if (clientId == null) {
            Client client = clientRepository.findBySource(servletRequest.getRemoteHost()).orElseThrow();
            useClientId = client.getId();
        }

        Page<Ticket> tickets = ticketRepository.findByClientId(useClientId,
                PageRequest.of(page, size, Sort.by("createdDate").descending()));
        return new CustomPage<>(tickets
                .map(t -> modelMapper.map(t, TicketResponse.class)));

    }

    public CustomPage<TicketResponse> getCustomerTicketsByEmail(String customerEmail, int page, int size) {

        Page<Ticket> tickets = ticketRepository.findByCustomerEmail(customerEmail,
                PageRequest.of(page, size, Sort.by("createdDate").descending()));
        return new CustomPage<>(tickets
                .map(t -> modelMapper.map(t, TicketResponse.class)));
    }

    public CustomPage<TicketResponse> getUserCreatedTickets(String username, int page, int size) {
        String useUsername = username;
        if (username == null) {
            useUsername = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }

        Page<Ticket> tickets = ticketRepository.findByCreatedBy(useUsername,
                PageRequest.of(page, size, Sort.by("createdDate").descending()));
        return new CustomPage<>(tickets
                .map(t -> modelMapper.map(t, TicketResponse.class)));
    }

    public TicketResponse updateTicket(UUID ticketId, TicketRequest request) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow();
        ticket.setStatus(request.getStatus());
        ticket.setPriority(request.getPriority());
        ticket.setCustomerName(request.getCustomerName());
        ticket.setCustomerEmail(request.getCustomerEmail());
        ticket.setTitle(request.getTitle());
        ticket.setDescription(request.getDescription());
        ticket.setCustomerPhone(request.getCustomerPhone());
        ticket.setModule(request.getModule());

        ticket = ticketRepository.save(ticket);
        return modelMapper.map(ticket, TicketResponse.class);
    }

    public TicketResponse assignTicket(UUID ticketId, UUID userId) {
        ApplicationUser user = applicationUserRepository.findById(userId)
                .orElseThrow();
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow();
        ticket.setAssignedTo(user);
        ticket = ticketRepository.save(ticket);
        return modelMapper.map(ticket, TicketResponse.class);
    }

    public List<CommentDTO> getTicketComments(UUID ticketId) {
        List<Comment> comments = commentRepository.findByTicketIdOrderByCreatedDateAsc(ticketId);
        return comments.stream()
                .map(comment -> modelMapper.map(comment, CommentDTO.class))
                .collect(Collectors.toList());
    }

    public CommentDTO addTicketComment(UUID ticketId, CommentDTO commentDTO) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow();
        Comment comment = modelMapper.map(commentDTO, Comment.class);
        comment.setTicket(ticket);
        comment = commentRepository.save(comment);
        return modelMapper.map(comment, CommentDTO.class);
    }

    public void removeTicketComment(UUID commentId) {
        commentRepository.deleteById(commentId);
    }
}
